package allpointech.franchisee.network.http.resource.data;


import allpointech.franchisee.network.http.HttpInfo;
import allpointech.franchisee.network.http.MultiPartData;
import allpointech.franchisee.network.http.resource.ResBaseHttp;

public class ResFranchiseeStoreFav extends ResBaseHttp {
    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
        public static final String sid = "sid";
    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String message = "message";
    }

    // request
    public void setParameter(String... params) {
        setMultipartData(MultiPartData.FORM, KEY_REQ.sid, params[0]);
    }

    public void setToken(String token) {
        super.setToken(token);
    }



    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET_PARAM;
    }
}
