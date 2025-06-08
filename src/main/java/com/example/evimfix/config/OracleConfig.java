package com.example.evimfix.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class OracleConfig {
    @Value("${ngrok.url:${env.NGROK_URL:4.tcp.eu.ngrok.io:13234/EVIMFIX}}")  // Checks both properties and environment
    private String ngrokUrl;
    
    @Bean
    public DataSource oracleDataSource() throws SQLException, IOException {
        HikariDataSource ds = new HikariDataSource();
        
        // Get Ngrok URL - you might want to make this dynamic
        
        //ds.setJdbcUrl("jdbc:oracle:thin:@" + ngrokUrl);
        ds.setJdbcUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=4.tcp.eu.ngrok.io)(PORT=13234))(CONNECT_DATA=(SID=EVIMFIX)))");
        ds.setUsername("EVIMFIX");
        ds.setPassword("1qa2ws3ed");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        
        // Connection pool settings
        ds.setMaximumPoolSize(5);
        ds.setConnectionTimeout(30000); // 30 seconds
        ds.setIdleTimeout(600000); // 10 minutes
        ds.setMaxLifetime(1800000); // 30 minutes

        // Retry configuration for HikariCP
        ds.addDataSourceProperty("oracle.net.CONNECT_TIMEOUT", "30000");
        ds.addDataSourceProperty("oracle.jdbc.ReadTimeout", "60000");

        // Custom connection test query
        ds.setConnectionTestQuery("SELECT 1 FROM DUAL");

        // Validation settings
        ds.setValidationTimeout(5000);
        ds.setInitializationFailTimeout(60000);
        
        return ds;
    }
    
    // private String getCurrentNgrokUrl() {
    //     //return "//7.tcp.eu.ngrok.io:19990/EVIMFIX";

    //     return System.getenv("NGROK_DB_URL") != null ? 
    //            System.getenv("NGROK_DB_URL") : 
    //            "//7.tcp.eu.ngrok.io:19990/EVIMFIX";
    // }

    // private String getCurrentNgrokUrl() throws IOException {
    //     // Option 1: Get from Ngrok API (requires ngrok running on default port 4040)
    //     try {
    //         URL apiUrl = new URL("http://localhost:4040/api/tunnels");
    //         HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
    //         String json = new Scanner(conn.getInputStream()).useDelimiter("\\Z").next();
            
    //         JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
    //         return obj.getAsJsonArray("tunnels")
    //                  .get(0).getAsJsonObject()
    //                  .get("public_url").getAsString()
    //                  .replace("tcp://", "//");
    //     } catch (Exception e) {
    //         // Fallback to environment variable
    //         String envUrl = System.getenv("NGROK_DB_URL");
    //         if (envUrl != null) return envUrl;
            
    //         throw new IOException("Cannot get Ngrok URL from API or environment", e);
    //     }
    // }
}