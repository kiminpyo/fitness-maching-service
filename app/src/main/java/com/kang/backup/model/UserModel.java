package com.kang.backup.model;

public class UserModel {

    // 이메일
    private String id;
    // 패스워드
    private String pwd;
    // 유저 이름
    private String username;
    // 전화 번호
    private String callnum;
    //프로필 사진 경로
    private String imageurl;
    //성별
    private String gender;
    // 트레이너 여부
    private boolean trainer = false;
    // 전문분야 (1개)
    private String major = "";
    // 지역(1개)
    private String area = "";
    // 경력(3개)
    private String career_1 = "";
    private String career_2 = "";
    private String career_3 = "";
    // 자격증(3개)
    private String cert_1 = "";
    private String cert_2 = "";
    private String cert_3 = "";
    // 트레이너 소개
    private String introduce = "";
    // 퍼블리셔
    private String publisher = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCallnum() {
        return callnum;
    }

    public void setCallnum(String callnum) {
        this.callnum = callnum;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isTrainer() {
        return trainer;
    }

    public void setTrainer(boolean trainer) {
        this.trainer = trainer;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCareer_1() {
        return career_1;
    }

    public void setCareer_1(String career_1) {
        this.career_1 = career_1;
    }

    public String getCareer_2() {
        return career_2;
    }

    public void setCareer_2(String career_2) {
        this.career_2 = career_2;
    }

    public String getCareer_3() {
        return career_3;
    }

    public void setCareer_3(String career_3) {
        this.career_3 = career_3;
    }

    public String getCert_1() {
        return cert_1;
    }

    public void setCert_1(String cert_1) {
        this.cert_1 = cert_1;
    }

    public String getCert_2() {
        return cert_2;
    }

    public void setCert_2(String cert_2) {
        this.cert_2 = cert_2;
    }

    public String getCert_3() {
        return cert_3;
    }

    public void setCert_3(String cert_3) {
        this.cert_3 = cert_3;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
