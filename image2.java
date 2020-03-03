
/**
         * Uploading the file to server
         * */
    public int uploadFile(String sourceFile, String fileName, String cityname, String accesskey) {
//        String fileName = sourceFileUri;
        //  Log.e("CameraActivity ","uploadfile "+ sourceFile +"  filename "+ fileName+"  cityname "+ cityname+ " access "+ accesskey);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        int serverResponseCode = 0;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "***";
        int bytesRead, bytesAvailable, bufferSize;
       // byte[] buffer;
        int maxBufferSize = 1 * 1024 ;
        //  File sourceFile = new File(sourceFileUri);
        byte[] buffer = new byte[8192];
        int count;
        String response="";

        try {
            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
           // String newfoldername = foldername.replace(" ","");
            URL url = new URL("http://www. your demain here/uploadImageFile.php?city="+ URLEncoder.encode(cityname, "UTF-8")
                    +"&random="+ URLEncoder.encode(accesskey, "UTF-8"));
               // Log.e( "CemeraActivity "," upload file url "+ url.toString() );
            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
           // conn.setChunkedStreamingMode(1024);
          //  conn.setFixedLengthStreamingMode(1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF=8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            try {
                while (bytesRead > 0) {
                    try {
                        dos.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        response = "outofmemoryerror";
                     //   Log.e(TAG, " error in upload image "+ e.toString());
                        return 411;
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
               // Log.e(TAG, " error in upload image 2 "+ e.toString());
                response = "error";
                return 411;
            }
 /*           while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
*/
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();


            // Log.e("Camera Activity ", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                 // Log.e(TAG," upload image succesfull inside 200");
                BufferedReader in=new BufferedReader(  new InputStreamReader(  conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                // Log.e("Camera activity "," sb.tostring is " + sb.toString());
                if (sb.toString().equals("Image upload Success")){
                    serverResponseCode= 201;
                    imguploadDone =true;
                }else if (sb.toString().equals("server image upload fail")){
                    imguploadDone =false;
                }else if(sb.toString().equals("invalid user access")){
                    imguploadAuthFail =true;
                }else if (sb.toString().equals("error in operation")){
                    imguploadOperFail= true;
                }
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {


             //  Log.e(TAG, "upload image 3 error: " + ex.getMessage(), ex);
        } catch (Exception e) {

          //  Log.e(TAG," upload image 4 error "  + e.getMessage(), e);
        }

        return serverResponseCode;

    }