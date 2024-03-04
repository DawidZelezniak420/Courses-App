package com.zelezniak.emailservice.rabbitmq;

import com.zelezniak.emailservice.dto.EmailInfo;
import com.zelezniak.emailservice.exception.CustomErrors;
import com.zelezniak.emailservice.exception.EmailException;
import com.zelezniak.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class Listener {

    private final EmailService emailService;

    private static final String QUEUE_NAME = "emails-queue";

    @RabbitListener(queues = QUEUE_NAME)
    public void getEmailInfoFromQueue(EmailInfo emailInfo) {
        log.info("EMAIL INFO IIIIIIIIIIIIIIIIIIIIIIIIIIIII     "+emailInfo);
        if (emailInfo == null) { throw new EmailException(CustomErrors.EMAIL_INFO_NOT_FOUND);}
        emailService.prepareEmail(emailInfo);
    }
}
