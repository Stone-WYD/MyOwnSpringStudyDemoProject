package com.itheima.a23;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;
import java.util.List;

/**
* @Description: 这里的转换都是针对 reqest 中的 parameter 中而言的，并不包含 body 中的数据
* @Author: Stone
* @Date: 2023/10/23
*/
public class TestServletDataBinderFactory {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1999|01|02"); // 1999/01/02 dataBinder 默认可解析
        request.setParameter("address.name", "西安");

        User target = new User();
        // 不使用 factory 时，可以直接从 request 中提取属性值，注入 target 中，但是其中日期类型的格式是固定的
        // ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(target);
        // dataBinder.bind(new ServletRequestParameterPropertyValues(request));

        // 使用 factory，可以扩展参数绑定或者加入一些自定义参数绑定
        // "1. 用工厂, 无转换功能"
        // ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);

        // "2. 用 @InitBinder 转换"          PropertyEditorRegistry PropertyEditor
        /*InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(method), null);*/

        // "3. 用 ConversionService 转换"    ConversionService Formatter
/*        FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("用 ConversionService 方式扩展转换功能"));
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);*/

        // "4. 同时加了 @InitBinder 和 ConversionService"
        InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
        FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("用 ConversionService 方式扩展转换功能"));
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(/*优先级更高：List.of(method)*/ null, initializer);

        // "5. 使用默认 ConversionService 转换"
        /*ApplicationConversionService service = new ApplicationConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);*/

        // 通过 dataBinder 绑定 request 中的参数
        WebDataBinder dataBinder = factory.createBinder(/*new ServletWebRequest(request) 不传递没有影响*/null , target, "user");
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(target);
    }

    static class MyController {
        @InitBinder
        public void aaa(WebDataBinder dataBinder) {
            // 扩展 dataBinder 的转换器
            dataBinder.addCustomFormatter(new MyDateFormatter("用 @InitBinder 方式扩展的"));
        }
    }

    public static class User {
        // @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "User{" +
                   "birthday=" + birthday +
                   ", address=" + address +
                   '}';
        }
    }

    public static class Address {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Address{" +
                   "name='" + name + '\'' +
                   '}';
        }
    }
}
