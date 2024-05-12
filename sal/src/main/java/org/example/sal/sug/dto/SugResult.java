
package org.example.sal.sug.dto;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SugResult {

    @Expose
    private List<G> g;
    @Expose
    private Boolean p;
    @Expose
    private String q;
    @Expose
    private String queryid;
    @Expose
    private String slid;

    public List<G> getG() {
        return g;
    }

    public void setG(List<G> g) {
        this.g = g;
    }

    public Boolean getP() {
        return p;
    }

    public void setP(Boolean p) {
        this.p = p;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

}
