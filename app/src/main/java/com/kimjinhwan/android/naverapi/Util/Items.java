package com.kimjinhwan.android.naverapi.Util;

import java.io.Serializable;

/**
 * Created by XPS on 2017-09-05.
 */

// 네이버 서버로 부터 받아온 json 데이터를 이 클래스에 저장한다.

public class Items implements Serializable{

        private String title;

        private String hprice;

        private String link;

        private String image;

        private String lprice;

        private String productType;

        private String productId;

        private String mallName;

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getHprice ()
        {
            return hprice;
        }

        public void setHprice (String hprice)
        {
            this.hprice = hprice;
        }

        public String getLink ()
        {
            return link;
        }

        public void setLink (String link)
        {
            this.link = link;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getLprice ()
        {
            return lprice;
        }

        public void setLprice (String lprice)
        {
            this.lprice = lprice;
        }

        public String getProductType ()
        {
            return productType;
        }

        public void setProductType (String productType)
        {
            this.productType = productType;
        }

        public String getProductId ()
        {
            return productId;
        }

        public void setProductId (String productId)
        {
            this.productId = productId;
        }

        public String getMallName ()
        {
            return mallName;
        }

        public void setMallName (String mallName)
        {
            this.mallName = mallName;
        }

        @Override
        public String toString()
        {
            return "[title = "+title+", hprice = "+hprice+", link = "+link+", image = "+image+", lprice = "+lprice+", productType = "+productType+", productId = "+productId+", mallName = "+mallName+"]";
        }
}

