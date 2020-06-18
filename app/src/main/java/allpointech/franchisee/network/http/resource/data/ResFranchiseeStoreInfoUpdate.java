package allpointech.franchisee.network.http.resource.data;


import allpointech.franchisee.network.http.HttpInfo;
import allpointech.franchisee.network.http.MultiPartData;
import allpointech.franchisee.network.http.resource.ResBaseHttp;

public class ResFranchiseeStoreInfoUpdate extends ResBaseHttp {

    @Override
    public String makeType() throws Exception {
        return HttpInfo.PUT;
        //return super.makeType();
    }

    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
        public static final String sid = "sid";
        public static final String name = "name";
        public static final String ssn = "ssn";
        public static final String sectors = "sectors";
        public static final String phone = "phone";
        public static final String fax = "fax";
        public static final String zipcode = "zipcode";
        public static final String addr = "addr";
        public static final String address = "address";
        public static final String gender = "gender";
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
        setMultipartData(MultiPartData.FORM, KEY_REQ.name, params[1]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.ssn, params[2]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.sectors, params[3]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.phone, params[4]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.fax, params[5]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.zipcode, params[6]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.addr, params[7]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.address, params[8]);
        setMultipartData(MultiPartData.FORM, KEY_REQ.gender, params[9]);
    }
}
