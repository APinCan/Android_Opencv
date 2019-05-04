 package com.example.textrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

 public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE=1001;
    private static final int PICK_FROM_ALBUM = 2000;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        button =(Button)findViewById(R.id.btnload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        imageView=(ImageView)findViewById(R.id.imageView);

    }

    public void checkPermission(){
        //퍼미션을 체크 권한이 있는경우 PackageManager.PERMISSION_GRANTED반환
        //권한이 없는 경우 PackageManager.PERMISSION_DENIED반환
        int  permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionArray[] = {
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE),
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA),
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
            //사용자에게 권한 요청할때 그 권한이 필요한 이유 사용자에게 설명
            //설명해 주기 위해 shouldShowRequestPermissionRationale 메서드 사용
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
            }
            //앱에 권한이 없는경우 reqeustPermissions 메서드를 호출해 권한 요청 후 사용자가 응답한 결과를 콜백
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다2", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //사용자가 응답하면 시스템은 onRequestPermsiionReuslt()메서드 호출해 사용자 응답을 전달
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "승인 허가",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "승인 허가받지 않았음", Toast.LENGTH_LONG).show();
                }
        }

        return ;
     }

     //앨범에서 선택한 이미지를 가져옴
     private void goToAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_FROM_ALBUM){
            if(resultCode==RESULT_OK){
                try{
                    //선택한 이미지에서 비트맵 생성
                    InputStream in=getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    //이미지표시
                    imageView.setImageBitmap(img);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
     }
 }
