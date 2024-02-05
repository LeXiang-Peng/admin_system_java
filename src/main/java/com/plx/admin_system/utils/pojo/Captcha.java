package com.plx.admin_system.utils.pojo;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author plx
 */
@Getter
@Setter
@Component
public class Captcha {
    public static final Integer WIDTH = 200;
    public static final Integer HEIGHT = 100;
    public static final Integer COUNT = 4;
    public static final Integer AMBIGUITY = 30;
    private AbstractCaptcha lineCaptcha;
    private Integer width;
    private Integer height;
    private String captcha;
    private Integer count;
    private Integer ambiguity;
    //constructor
    public Captcha() {
        this.count = COUNT;
        this.lineCaptcha = new LineCaptcha(WIDTH, HEIGHT, count, AMBIGUITY);
        this.captcha = lineCaptcha.getCode();
    }
    public Captcha(Integer count) {
        this.count = count;
        this.lineCaptcha = new LineCaptcha(WIDTH, HEIGHT, count, AMBIGUITY);
        this.captcha = lineCaptcha.getCode();
    }
    public Captcha(Integer count, Integer ambiguity) {
        this.ambiguity = ambiguity;
        this.count = count;
        this.lineCaptcha = new LineCaptcha(WIDTH, HEIGHT, count, ambiguity);
        this.captcha = lineCaptcha.getCode();
    }
    public Captcha(Integer width, Integer height, Integer count, Integer ambiguity) {
        this.width = width;
        this.height = height;
        this.count = count;
        this.ambiguity = ambiguity;
        this.lineCaptcha = new LineCaptcha(width, height, count, ambiguity);
        this.captcha = lineCaptcha.getCode();
    }

}
