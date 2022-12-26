package com.future.student;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.future.R;
import com.future.common.Constants;
import com.future.util.AvatarUtil;
import com.future.util.LocalDateTimeUtil;
import com.future.util.PictureUtils;
import com.future.util.SharedPreferencesUtils;
import com.future.util.UUIDUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    private static final String ARG_RECORD_NAME = "record_name";
    private static final int REQUEST_PHOTO = 3;
    private static final int REQUEST_GALLERY = 4;
    private static final int REQUEST_PHOTO2 = 5;
    private static final int REQUEST_GALLERY2 = 6;


    private String mRecordName;
    private static final int REQUEST_DATE = 0;

    //组件
    private EditText recordName;
    private Button recordDateBtn;
    private EditText recordMoneyText;
    private ImageView photoImg;
    private ImageView receiptImg;
    private ImageButton takePhotoBtn;
    private ImageButton takeReceiptBtn;
    private EditText checkEditText;
    private EditText state;

    private Button addRecordBtn;
    private Button updateRecordBtn;
    private Button questionBtn;
    private RecyclerView questionRecyclerView;

    private String role;

    private Record mRecord;

    private File mPhotoFile;
    private File mReceiptFile;

    private ImageLoader imageLoader;

    public RecordFragment() {
        // Required empty public constructor
    }

    public static RecordFragment newInstance(String recordName) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        if(recordName != null) {
            args.putString(ARG_RECORD_NAME, recordName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecordName = getArguments().getString(ARG_RECORD_NAME);
        }
        if(mRecordName != null) {
            mRecord = RecordLab.get(getActivity()).getRecord(mRecordName);
        }else{
            mRecord = new Record();
        }


        //设置id
        if(mRecordName == null) {
            mRecord.setId(UUIDUtil.getUUID32().toString());
        }
        mPhotoFile = RecordLab.get(getActivity()).getPhotoFile(mRecord);
        mReceiptFile = RecordLab.get(getActivity()).getReceiptFile(mRecord);

        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.logo)    //设置下载过程中图片显示
                .showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(
                getContext().getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        //初始化视图
        recordName = view.findViewById(R.id.record_name);
        recordDateBtn = view.findViewById(R.id.record_date);
        recordMoneyText = view.findViewById(R.id.record_money);
        photoImg = view.findViewById(R.id.photo_img);
        receiptImg = view.findViewById(R.id.receipt_img);
        takePhotoBtn = view.findViewById(R.id.take_photo);
        takeReceiptBtn = view.findViewById(R.id.take_receipt);
        checkEditText = view.findViewById(R.id.check_edittext);
        state = view.findViewById(R.id.state);
        addRecordBtn = view.findViewById(R.id.add_record);
        updateRecordBtn = view.findViewById(R.id.update_record);
        questionBtn = view.findViewById(R.id.question_btn);

        //更新图片
        updatePhoto();

        role = (String) SharedPreferencesUtils.getParam(getContext(), "role", "");
        if(mRecordName == null) {
            updateRecordBtn.setVisibility(View.GONE);
            recordDateBtn.setEnabled(false);
            recordDateBtn.setText(LocalDateTimeUtil.getDate());
        }else {
            addRecordBtn.setVisibility(View.GONE);
            recordName.setText(mRecord.getRecordName());
            recordName.setEnabled(false);
            recordDateBtn.setText(mRecord.getRecordDate()+"");
            recordMoneyText.setText(mRecord.getRecordMoney() + "");
            Integer recordState = mRecord.getState();
            if(recordState == 0) {
                state.setText("问题未关闭");
            }else{
                state.setText("问题已关闭");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FormBody.Builder params = new FormBody.Builder();
                        params.add("id",mRecord.getCheckId());
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(Constants.IP+"/user/findById")
                                .post(params.build())
                                .build();
                        Response response = client.newCall(request).execute();//执行发送的指令
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                        String username = jsonObject1.getString("username");
                        checkEditText.setText(username);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            //当选择的是添加记录时显示购物图片
            Log.e("file 的路径",mPhotoFile.getAbsolutePath());
            Bitmap bitmap = AvatarUtil.getAvatar(mPhotoFile);
            if(bitmap != null){
                Log.e("btimap is not null",bitmap.toString());
            }else{
                Log.e("bitmap is null","");
            }
            photoImg.setImageBitmap(AvatarUtil.getAvatar(mPhotoFile));


        }
        //如果是学生，则只有查看的权限和质疑的权限
        if(role.equals("3")) {
            updateRecordBtn.setVisibility(View.GONE);
            addRecordBtn.setVisibility(View.GONE);
            recordDateBtn.setEnabled(false);
            recordMoneyText.setEnabled(false);
            photoImg.setEnabled(false);
            receiptImg.setEnabled(false);
            takePhotoBtn.setEnabled(false);
            takeReceiptBtn.setEnabled(false);
            checkEditText.setEnabled(false);
            state.setEnabled(false);


        }


        //todo 日期选择器
        recordDateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = null;
                if(mRecord != null){
                    try {
                        dialog = DatePickerFragment.newInstance(LocalDateTimeUtil.LocalDateTimeToDate(mRecord.getRecordDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        dialog = DatePickerFragment.newInstance(null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                assert dialog != null;
                dialog.setTargetFragment(RecordFragment.this,REQUEST_DATE);
                dialog.show(fm,"RecordDate");

            }
        });

        //todo 从图库选择图片
        photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);

            }
        });

        receiptImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY2);
            }
        });

        //todo 点击拍照按钮获取图片
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.future.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);

            }
        });
        final Intent captureImage2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.future.fileprovider",mReceiptFile);
                captureImage2.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage2,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage2,REQUEST_PHOTO2);
            }
        });

        //todo 添加记录功能
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String record_name = recordName.getText().toString();
                String dateStr = recordDateBtn.getText().toString();

//                if(TextUtils.isEmpty(recordDateBtn.getText())) {
//                    LocalDateTime date = LocalDateTimeUtil.StrToLoaclDateTime(dateStr);
//                }
                String moneyStr = recordMoneyText.getText().toString();
//                if(TextUtils.isEmpty(recordMoneyText.getText())){
//                    Double money = Double.valueOf(moneyStr);
//                }

                String checkName = checkEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("username",checkName);
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/user/find")
                                    .post(params.build())
                                    .build();
                            Response response = client.newCall(request).execute();//执行发送的指令
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            String code = jsonObject.getString("code");
                            Log.e("code is",code.toString());
                            if(!code.equals("200")){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"核查人不存在",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                if(TextUtils.isEmpty(recordName.getText()) || TextUtils.isEmpty(recordMoneyText.getText())) {
                    Toast.makeText(getActivity(),"请检查信息是否填写完整",Toast.LENGTH_SHORT).show();
                }

                //添加
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"id\":\""+mRecord.getId()+"\",\n" +
                                    "    \"recordName\":\""+record_name+"\",\n" +
                                    "    \"recordDate\":\""+dateStr+"\",\n" +
                                    "    \"recordMoney\":\""+moneyStr+"\",\n" +
                                    "    \"recordMoneyNew\":\""+moneyStr+"\",\n" +
                                    "    \"photos\":\""+mRecord.getId()+"\",\n" +
                                    "    \"receipt\":\""+mRecord.getId()+"\",\n" +
                                    "    \"checkId\":\""+checkName+"\",\n" +
                                    "    \"state\":\"0\"\n" +
                                    "}";
                            //创建http客户端
                            OkHttpClient client = new OkHttpClient();
                            //创建http请求
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/record/add")
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))
                                    .build();
                            Response response = client.newCall(request).execute();//执行
                            //获取前端响应的结果，后端响应的是Result类型的json字符串，包含code，data，msg
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if(code == 200) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                                        //清空edittext
                                        recordName.setText("");
                                        recordMoneyText.setText("");
                                        photoImg.setImageResource(R.drawable.ic_launcher_background);
                                        receiptImg.setImageResource(R.drawable.ic_launcher_background);
                                        checkEditText.setText("");
                                        //跳转回列表页
                                        Intent intent = new Intent(getActivity(),NavActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"添加失败，记录名重复",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                }).start();

            }
        });

        //todo 修改记录/更新记录
        updateRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String record_name = recordName.getText().toString();
                String dateStrT = recordDateBtn.getText().toString();
                String dateStr = LocalDateTimeUtil.strRemoveT(dateStrT);
                String moneyStr = recordMoneyText.getText().toString();
                String checkName = checkEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("username",checkName);
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/user/find")
                                    .post(params.build())
                                    .build();
                            Response response = client.newCall(request).execute();//执行发送的指令
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            String code = jsonObject.getString("code");
                            Log.e("code is",code.toString());
                            if(!code.equals("200")){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"核查人不存在",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                if(TextUtils.isEmpty(recordName.getText()) || TextUtils.isEmpty(recordMoneyText.getText())) {
                    Toast.makeText(getActivity(),"请检查信息是否填写完整",Toast.LENGTH_SHORT).show();
                }

                //修改
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"id\":\""+mRecord.getId()+"\",\n" +
                                    "    \"recordName\":\""+record_name+"\",\n" +
                                    "    \"recordDate\":\""+dateStr+"\",\n" +
                                    "    \"recordMoney\":\""+mRecord.getRecordMoney()+"\",\n" +
                                    "    \"recordMoneyNew\":\""+moneyStr+"\",\n" +
                                    "    \"photos\":\""+mRecord.getId()+"\",\n" +
                                    "    \"receipt\":\""+mRecord.getId()+"\",\n" +
                                    "    \"checkId\":\""+checkName+"\",\n" +
                                    "    \"state\":\"0\"\n" +
                                    "}";
                            //创建http客户端
                            OkHttpClient client = new OkHttpClient();
                            //创建http请求
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/record/update")
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))
                                    .build();
                            Response response = client.newCall(request).execute();//执行
                            //获取前端响应的结果，后端响应的是Result类型的json字符串，包含code，data，msg
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if(code == 200) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                                        //清空edittext
                                        recordName.setText("");
                                        recordMoneyText.setText("");
                                        photoImg.setImageResource(R.drawable.ic_launcher_background);
                                        receiptImg.setImageResource(R.drawable.ic_launcher_background);
                                        checkEditText.setText("");
                                        //跳转回列表页
                                        Intent intent = new Intent(getActivity(),NavActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                }).start();

            }
        });
        return view;
    }


    private void updatePhoto() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            photoImg.setImageResource(R.drawable.ic_launcher_background);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            photoImg.setImageBitmap(bitmap);
        }

        if(mReceiptFile == null || !mReceiptFile.exists()) {
            receiptImg.setImageResource(R.drawable.ic_launcher_background);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mReceiptFile.getPath(),getActivity());
            receiptImg.setImageBitmap(bitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case REQUEST_PHOTO:
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.future.fileprovider",mPhotoFile);
                getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhoto();
            case REQUEST_GALLERY:
                if (data != null)
                {
                    // 得到图片的全路径
                    Uri photoUri1 = data.getData();
                    if(photoUri1 != null) {
                        Bitmap bitmap1 = small(imageLoader.loadImageSync(
                                photoUri1.toString()));
                        photoImg.setImageBitmap(bitmap1);
                    }

                }
            case REQUEST_PHOTO2:
                Uri uri2 = FileProvider.getUriForFile(getActivity(),"com.future.fileprovider",mReceiptFile);
                getActivity().revokeUriPermission(uri2,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhoto();
            case REQUEST_GALLERY2:
                if (data != null)
                {
                    // 得到图片的全路径
                    Uri photoUri1 = data.getData();
                    if(photoUri1 != null) {
                        Bitmap bitmap1 = small(imageLoader.loadImageSync(
                                photoUri1.toString()));
                        receiptImg.setImageBitmap(bitmap1);
                    }

                }
            case REQUEST_DATE:
//                if(resultCode != Activity.RESULT_OK){
//                    break;
//                }

                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                if(date != null) {
                    try {
                        Log.e("date:",LocalDateTimeUtil.DateToLocalDateTime(date).toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        mRecord.setRecordDate(LocalDateTimeUtil.DateToLocalDateTime(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("record fragment add 日期",mRecord.getRecordDate().toString());
                    recordDateBtn.setText(mRecord.getRecordDate().toString());
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }
    private Bitmap small(Bitmap bitmap)
    {
        try
        {

            Matrix matrix = new Matrix();
            matrix.postScale((float) 480 / bitmap.getWidth(), (float) 480
                    / bitmap.getWidth()); // 长和宽放大缩小的比例
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}