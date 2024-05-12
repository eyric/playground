package org.example.sal.sug;


import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import org.example.sal.sug.dto.SugResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "https://www.baidu.com")
public interface SugRecClient {

    @GET("/sugrec?pre=1&p=1&ie=utf-8&json=2&prod=pc&from=pc_web")
    SugResult sugRec(@Query("wd") String wd);
}
