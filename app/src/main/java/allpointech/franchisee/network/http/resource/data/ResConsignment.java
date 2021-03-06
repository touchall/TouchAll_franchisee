package allpointech.franchisee.network.http.resource.data;


import allpointech.franchisee.network.http.HttpInfo;
import allpointech.franchisee.network.http.resource.ResBaseHttp;

public class ResConsignment extends ResBaseHttp {

    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET_TEXT;
        //return super.makeType();
    }

    @Override
    public String makeURL() throws Exception {
        return "https://dev.touchall.co.kr/clause/useContent";
        //return super.makeURL();
    }

    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String consignment = "consignment";
    }

    // request
    public void setParameter(String... params) {
    }
}
