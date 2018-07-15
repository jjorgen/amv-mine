package org.nsu.dcis.amv.common;

/**
 * Created by jorgej2 on 7/15/2018.
 */
public class CalledMethod {

    String calledMethodName;
    String calledMethodDetail;

    public String getCalledMethodName() {
        return calledMethodName;
    }

    public void setCalledMethodName(String calledMethodName) {
        this.calledMethodName = calledMethodName;
    }

    public String getCalledMethodDetail() {
        return calledMethodDetail;
    }

    public void setCalledMethodDetail(String calledMethodDetail) {
        this.calledMethodDetail = calledMethodDetail;
    }

    @Override
    public String toString() {
        return "CalledMethod{" +
                "calledMethodName='" + calledMethodName + '\'' +
                ", calledMethodDetail='" + calledMethodDetail + '\'' +
                '}';
    }
}
