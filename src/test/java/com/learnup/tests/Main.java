package com.learnup.tests;

//import com.learnup.db.dao.ProductsMapper;
//import com.learnup.db.model.Products;
//import com.learnup.db.model.ProductsExample;
import java.io.InputStream;

import com.learnup.db.dao.ProductsMapper;
import com.learnup.db.model.Products;
import com.learnup.db.model.ProductsExample;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
@Slf4j
public class Main {
    static String resource = "mybatisConfig.xml";

    @SneakyThrows
    public static void main(String[] args) {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        System.out.println("Подключение установлено!");

        ProductsMapper productsMapper = sqlSession.getMapper(ProductsMapper.class);
        Products productByKey = productsMapper.selectByPrimaryKey(18412l);
        System.out.println(productByKey.getTitle());
        Products product = Products.builder()
                .category_id(1L)
                .price(250)
                .title("Berries")
                .build();
        productsMapper.insert(product);
        ProductsExample productsExample = new ProductsExample();
        productsExample.createCriteria()
                .andCategory_idEqualTo(product.getCategory_id())
                .andPriceEqualTo(product.getPrice())
                .andTitleEqualTo(product.getTitle());
        Long productId = productsMapper.selectByExample(productsExample).get(0).getId();
        System.out.println(productId);
        productsMapper.deleteByPrimaryKey(productId);
    }
}