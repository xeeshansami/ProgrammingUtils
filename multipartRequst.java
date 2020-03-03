package com.kalianey.oxapp.utils;
 
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
 
public class MultipartRequest extends Request<String>  {
 
    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private final File mImageFile;
    protected Map<String, String> headers;
    private String mBoundary;
    private Map<String, String> mParams;
    private String mFileFieldName;
    private String mFilename;
    private String mBodyContentType;
 
    public void setBoundary(String boundary) {
        this.mBoundary = boundary;
    }
 
    public MultipartRequest(String url, final Map<String, String> params, File imageFile, String filename, String fileFieldName, ErrorListener errorListener, Listener<String> listener ){
        super(Method.POST, url, errorListener);
 
        mListener = listener;
        mImageFile = imageFile;
        mParams = params;
        mFileFieldName = fileFieldName;
        mFilename = filename;
 
        buildMultipartEntity();
    }
 
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
 
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Accept", "application/json");
        headers.put("X-Requested-With", "XMLHTTPRequest");
        headers.put("User-Agent", "KaliMessenger");
        return headers;
    }
 
    private void buildMultipartEntity(){
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            mBuilder.addTextBody(entry.getKey(), entry.getValue());
        }
        mBuilder.addBinaryBody(mFileFieldName, mImageFile, ContentType.create("image/jpg"), mFilename);
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }
    @Override
    public String getBodyContentType(){
        return mBodyContentType;
    }
    @Override
    public byte[] getBody() throws AuthFailureError{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            HttpEntity entity = mBuilder.build();
            mBodyContentType = entity.getContentType().getValue();
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }
 
        return bos.toByteArray();
    }
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
 final String reqUrl = hostname+url;
        MultipartRequest imageUploadReq = new MultipartRequest(reqUrl,params,file,filename,fileField,
            new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Multipart Request Url: ", reqUrl);
                        Log.d("Multipart ERROR", "error => " + error.toString());
                        completion.onCompletion(error.toString());
                        displayVolleyResponseError(error);
                    }
                },
            new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MediaSent Response", response);
                        completion.onCompletion(response);
 
                    }
                }
            ) {
 
            /* The following method sets the cookies in the header, I needed it for my server 
             but you might want to remove it if it is not useful in your case */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                CookieManager manager = AppController.getInstance().getCookieManager();
                List<HttpCookie> cookies = manager.getCookieStore().getCookies();
                String cookie = "";
                for (HttpCookie eachCookie : cookies) {
                    String cookieName = eachCookie.getName().toString();
                    String cookieValue = eachCookie.getValue().toString();
                    cookie += cookieName + "=" + cookieValue + "; ";
                }
                headers.put("Cookie", cookie);
                return headers;
            }
 
        };
 
        imageUploadReq.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
 
        AppController.getInstance().addToRequestQueue(imageUploadReq);
    }
	

public void updateAvatar(File media, final ApiResponse<ApiResult> completion) {

        String url = "owapi/user/update/avatar";

        String uuid = UUID.randomUUID().toString();
        String boundary = "----------------------------"+uuid;
        String fileName = uuid+".jpg";
        Map<String,String> params = new HashMap<>();
        params.put("ajaxFunc", "ajaxUploadImage");
        params.put("pluginKey", "mailbox");

        this.RequestMultiPart(media, fileName, boundary, url, "file", params, new ApiResponse<String>() {
            @Override
            public void onCompletion(String result) {

                ApiResult res = new ApiResult();
                res.success = false;
                try {
                    JSONObject data = new JSONObject(result);
                    res.success = data.getBoolean("success");
                    if (res.success) {
                        try {
                            res.data = data.getString("data");
                        } catch (JSONException x) {
                            x.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                completion.onCompletion(res);
            }
        });

    }
public void updateAvatar(File media, final ApiResponse<ApiResult> completion) {
 
        String url = "owapi/user/update/avatar";
 
        String uuid = UUID.randomUUID().toString();
        String boundary = "----------------------------"+uuid;
        String fileName = uuid+".jpg";
        Map<String,String> params = new HashMap<>();
        params.put("ajaxFunc", "ajaxUploadImage");
        params.put("pluginKey", "mailbox");
 
        this.RequestMultiPart(media, fileName, boundary, url, "file", params, new ApiResponse<String>() {
            @Override
            public void onCompletion(String result) {
 
                ApiResult res = new ApiResult();
                res.success = false;
                try {
                    JSONObject data = new JSONObject(result);
                    res.success = data.getBoolean("success");
                    if (res.success) {
                        try {
                            res.data = data.getString("data");
                        } catch (JSONException x) {
                            x.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                completion.onCompletion(res);
            }
        });
 
    }