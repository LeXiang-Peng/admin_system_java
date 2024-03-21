package com.plx.admin_system.utils.pojo;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author plx
 */
@Getter
@Setter
@Component
public class Captcha extends AbstractCaptcha {
    private static final Integer WIDTH = 200;
    private static final Integer HEIGHT = 100;
    private static final Integer COUNT = 4;
    private static final Integer LINECOUNT = 50;
    private Integer visits;
    private Boolean isUsed;

    public Captcha() {
        super(WIDTH, HEIGHT, COUNT, LINECOUNT);
        this.reshapeToMathGenerator();
        this.visits = 0;
        this.isUsed = false;
    }

    @Override
    protected Image createImage(String code) {
        BufferedImage image = new BufferedImage(this.width, this.height, 1);
        Graphics2D g = GraphicsUtil.createGraphics(image, (Color) ObjectUtil.defaultIfNull(this.background, Color.WHITE));
        this.drawInterfere(g);
        this.drawString(g, code);
        return image;
    }

    public Boolean verifyCode(String code) {
        Boolean result = isUsed ? false : super.verify(code);
        isUsed = true;
        return result;
    }

    public void reshapeToRandomGenerator() {
        super.setGenerator(new RandomGenerator(COUNT));
    }

    public void reshapeToMathGenerator() {
        super.setGenerator(new MathGenerator());
    }

    public void createCaptchaImage(HttpServletResponse response) {
        this.recreate();
        this.isUsed = false;
        try {
            super.write(response.getOutputStream());
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recreate() {
        super.createCode();
    }

    private void drawInterfere(Graphics2D g) {
        ThreadLocalRandom random = RandomUtil.getRandom();

        for (int i = 0; i < this.interfereCount; ++i) {
            int xs = random.nextInt(this.width);
            int ys = random.nextInt(this.height);
            int xe = xs + random.nextInt(this.width / 8);
            int ye = ys + random.nextInt(this.height / 8);
            g.setColor(ImgUtil.randomColor(random));
            g.drawLine(xs, ys, xe, ye);
        }

    }

    private void drawString(Graphics2D g, String code) {
        if (null != this.textAlpha) {
            g.setComposite(this.textAlpha);
        }

        GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height);
    }

}
