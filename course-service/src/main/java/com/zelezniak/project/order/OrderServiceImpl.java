package com.zelezniak.project.order;

import com.zelezniak.project.course.Course;
import com.zelezniak.project.author.CourseAuthor;
import com.zelezniak.project.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Course course, CourseAuthor author) {
        Order order = Order.builder()
                .orderId(generateOrderId())
                .totalPrice(course.getPrice())
                .orderedCourse(course)
                .dateCreated(LocalDateTime.now())
                .build();
        order.addUser(author);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order createOrder(Course course, Student student) {
        Order order = Order.builder()
                .orderId(generateOrderId())
                .totalPrice(course.getPrice())
                .orderedCourse(course)
                .dateCreated(LocalDateTime.now())
                .build();
        order.addUser(student);
        orderRepository.save(order);
        return order;
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }
}