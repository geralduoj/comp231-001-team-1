package com.comp231.easypark.psm;

public class PSMApplication {
    private String docId;
    private String fullName;
    private String address; // this is the address of their business
    private String email;

    public PSMApplication() { }

    public void setDocId(String docId) { this.docId = docId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAddress(String address) { this.address = address; }
    public void setEmail(String email) { this.email = email; }

    public String getDocId() { return this.docId; }
    public String getFullName() { return this.fullName; }
    public String getAddress() { return this.address; }
    public String getEmail() { return this.email; }
}
