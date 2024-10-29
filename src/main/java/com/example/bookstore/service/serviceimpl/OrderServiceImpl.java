package com.example.bookstore.service.serviceimpl;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.bookstore.dao.BookDao;
import com.example.bookstore.dao.MemberDao;
import com.example.bookstore.dao.OrderDao;
import com.example.bookstore.dto.order.BuyItem;
import com.example.bookstore.dto.order.CreateOrderRequest;
import com.example.bookstore.dto.order.OrderQueryParams;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Member;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.service.OrderService;


@Service
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private MemberDao memberDao;

    // 建立購物訂單
    @Transactional
    @Override
    public Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest) {

        Member member = memberDao.getMemberById(memberId);

        // 如果會員不存在，拋出錯誤
        if (member == null) {
            log.warn("無法建立訂單，因為會員 ID: {} 不存在" , memberId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 計算訂單總金額 & 更新庫存
        Integer totalAmount = 0; // 初始化訂單總金額
        List<OrderItem> orderItemList = new ArrayList<>(); // 建立訂單商品列表 
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) { // 逐筆取出購買商品
            Book book = bookDao.getBookInfoById(buyItem.getProductId()); // 透過商品編號取得商品資訊 

            if (book == null) {
                // 如果商品不存在，拋出錯誤
                log.warn("無法建立訂單，因為商品 ID: {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            if (book.getStock() < buyItem.getQuantity()) {
                // 如果庫存不足，拋出錯誤
                log.warn("無法建立訂單，商品 ID: {} 庫存不足 (當前庫存: {}, 購買數量: {})", buyItem.getProductId(), book.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 更新庫存( 庫存減去購買數量 )
            bookDao.updateStock(book.getBookId(), book.getStock() - buyItem.getQuantity());
            
            // 計算訂單總金額
            int amount = buyItem.getQuantity() * book.getPrice();
            totalAmount = totalAmount + amount;
            log.info("新增訂單項目 - 商品 ID: {}, 數量: {}, 單價: {}, 小計: {}, 訂單總金額累計: {}", buyItem.getProductId(), buyItem.getQuantity(), book.getPrice(), amount, totalAmount);

            // 建立訂單商品
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            // 加入訂單商品列表
            orderItemList.add(orderItem);
        }

        // 建立商品訂單生成訂單編號
        Integer orderId = orderDao.createOrder(memberId, totalAmount);
        orderDao.createOrderItem(orderId, orderItemList);
        log.info("成功建立訂單 - 訂單 ID: {}, 會員 ID: {}, 總金額: {}", orderId, memberId, totalAmount);

        return orderId;
    }

    // 透過訂單編號查詢訂單
    @Override    
    public Order getOrderById(Integer orderId) {

        // 取得訂單ID
        Order order = orderDao.getOrderById(orderId);

        // 取得訂單商品列表透過訂單ID
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);
        
        return order;
    }

    // 計算訂單購物商品數量
    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    // 查詢購物訂單
    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);
        log.info("查詢訂單列表成功 - 總訂單數: {}", orderList.size());

        // 取得訂單商品列表透過訂單ID
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
            log.info("訂單 ID: {}, 訂單明細數量: {}", order.getOrderId(), orderItemList.size());
        }

        return orderList;
    }
}