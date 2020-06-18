package allpointech.franchisee.network.http.resource.data;


import allpointech.franchisee.network.http.MultiPartData;
import allpointech.franchisee.network.http.resource.ResBaseHttp;

public class ResLogOut extends ResBaseHttp {
    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
        public static final String member_idx = "member_idx";
    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
    }

    // request
    public void setParameter(String... params) {
        setMultipartData(MultiPartData.FORM, KEY_REQ.member_idx, params[0]);
    }
}
