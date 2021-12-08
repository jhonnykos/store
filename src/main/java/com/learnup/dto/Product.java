//package com.learnup.dto;
//
//import lombok.Builder;
//import lombok.Data;
//
//@Data
//@Builder
//public class Product {
//    private Integer id;
//    private String title;
//    private Integer price;
//    private String categoryTitle;
//
//    @Override
//    public String toString() {
//        return "Product{" +
//                "\"id\":" + id +
//                ", \"title\":'" + title + '\'' +
//                ", \"price\":" + price +
//                ", \"categoryTitle\":'" + categoryTitle + '\'' +
//                '}';
//    }
//}

package com.learnup.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Integer id;
    private String title;
    private Integer price;
    private String categoryTitle;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"title\":\"" + title + '\"' +
                ", \"price\":" + price +
                ", \"categoryTitle\":\"" + categoryTitle + '\"' +
                '}';
    }
}
