package com.example.tmall_springboot.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//import com.how2j.tmall_springboot.services.OrderService;

@Entity
@Data
@Table(name = "order_")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private String orderCode;
    private String address;
    private String post;
    private String receiver;
    private String mobile;
    private String userMessage;
    private Date createDate;
    private Date payDate;
    private Date deliveryDate;
    private Date confirmDate;
    private String status;

    @Transient
    private List<OrderItem> orderItems;
    @Transient
    private float total;
    @Transient
    private int totalNumber;
    @Transient
    private String statusDesc;

//    public String getStatusDesc(){
//        if(null!=statusDesc)
//            return statusDesc;
//        String desc = "未知";
//        switch(status) {
//            case OrderService.waitPay:
//                desc="待付";
//                break;
//            case OrderService.waitDelivery:
//                desc="待发";
//                break;
//            case OrderService.waitConfirm:
//                desc="待收";
//                break;
//            case OrderService.waitReview:
//                desc="等评";
//                break;
//            case OrderService.finish:
//                desc="完成";
//                break;
//            case OrderService.delete:
//                desc="刪除";
//                break;
//            default:
//                desc="未知";
//        }
//        statusDesc = desc;
//        return statusDesc;
//    }

}
