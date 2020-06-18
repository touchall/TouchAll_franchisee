package allpointech.franchisee.network.http.resource.data;


import allpointech.franchisee.network.http.HttpInfo;
import allpointech.franchisee.network.http.resource.ResBaseHttp;

public class ResFranchiseeOwnerInfo extends ResBaseHttp {
    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {

    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String message = "message";
    }

    // request
    public void setParameterQuestion(String... params) {
        setGetUrlData(params[0]);
    }

    public void setToken(String token) {
        super.setToken(token);
    }



    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET;
    }
}
