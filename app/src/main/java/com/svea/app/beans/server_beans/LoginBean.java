package com.svea.app.beans.server_beans;

public class LoginBean {


    /**
     * sid : 6
     * action : login
     * status : OK
     * value : {"fname":"firstname","lname":"lastname","email":"ykbeta@gmail.com","phone":"+919999999999","wallet":"35","status":"1"}
     */

    private int sid;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String action;
    private String status;
    private ValueBean value;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * fname : firstname
         * lname : lastname
         * email : ykbeta@gmail.com
         * phone : +919999999999
         * wallet : 35
         * status : 1
         */

        private String fname;
        private String lname;
        private String email;
        private String phone;
        private String wallet;
        private String status;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
