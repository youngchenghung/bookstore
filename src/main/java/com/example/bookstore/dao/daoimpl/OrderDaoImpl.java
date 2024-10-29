package com.example.bookstore.dao.daoimpl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.example.bookstore.dao.OrderDao;
import com.example.bookstore.dto.order.OrderQueryParams;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.rowmapper.OrderRowMapper;
import com.example.bookstore.rowmapper.OrderItemRowMapper;

@Component
public class OrderDaoImpl implements OrderDao{

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 建立購物訂單
    @Override
    public Integer createOrder(Integer member_id, Integer totalAmount) {
        String sql = "INSERT INTO `order` (member_id, total_amount, created_date, last_modified_date) VALUES (:member_id, :total_amount, :created_date, :last_modified_date)";
    
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("total_amount", totalAmount);

        Date now = new Date();
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    // 建立購物訂單商品
    @Override
    public void createOrderItem(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) VALUES (:orderId, :productId, :quantity, :amount)";

        // 批次新增 order_item 資料
        MapSqlParameterSource[] batch = new MapSqlParameterSource[orderItemList.size()];

        // 將 order_item 資料放入 batch
        // 透過 batchUpdate 一次新增多筆資料
        for (int i = 0 ; i < orderItemList.size() ; i++) {
            OrderItem orderItem = orderItemList.get(i);

            batch[i] = new MapSqlParameterSource();
            batch[i].addValue("orderId", orderId);
            batch[i].addValue("productId", orderItem.getProductId());
            batch[i].addValue("quantity", orderItem.getQuantity());
            batch[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }


    // 透過訂單編號查詢訂單
    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, member_id, total_amount, created_date, last_modified_date FROM `order` WHERE order_id = :order_id";

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);

        // 購物單列表
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        // 如果有購物單
        if (orderList.size() > 0) {
            return orderList.get(0);
        }

        else {
            return null;
        }
    }


    // 透過訂單編號查詢訂單商品
    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, b.book_id" +
                    " FROM order_item oi" +
                    " JOIN book as b ON oi.product_id = b.book_id" +
                    " WHERE oi.order_id = :order_id";

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);

        // 購物單商品列表
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }


    // 計算訂單購物商品數量
    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT COUNT(*) FROM `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 如果有傳入會員ID
        if (orderQueryParams.getMemberId() != null) {
            sql = sql + " AND member_id = :member_id";
            map.put("member_id", orderQueryParams.getMemberId());
        }

        // 取得總筆數
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }


    // 購物單列表
    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, member_id, total_amount, created_date, last_modified_date FROM `order` WHERE 1 = 1";

        Map<String, Object> map = new HashMap<>();
        
        // 如果有傳入會員ID
        if (orderQueryParams.getMemberId() != null) {
            sql = sql + " AND member_id = :member_id";
            map.put("member_id", orderQueryParams.getMemberId());
        }

        // 排序
        sql = sql + " ORDER BY created_date DESC";

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        // 購物單列表
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }


}
