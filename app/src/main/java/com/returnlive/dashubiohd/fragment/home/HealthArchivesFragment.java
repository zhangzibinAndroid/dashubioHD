package com.returnlive.dashubiohd.fragment.home;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HomeActivity;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.ErrorCodeBean;
import com.returnlive.dashubiohd.bean.HealthArchivesBean;
import com.returnlive.dashubiohd.constant.ErrorCode;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.returnlive.dashubiohd.constant.CityArray.city;
import static com.returnlive.dashubiohd.constant.CityArray.county;
import static com.returnlive.dashubiohd.constant.CityArray.province;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:18
 * 描述： 健康档案
 */
public class HealthArchivesFragment extends BaseFragment {

    private static final String TAG = "HealthArchivesFragment";
    @BindView(R.id.img_card)
    ImageView imgCard;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.tv_man)
    TextView tvMan;
    @BindView(R.id.tv_woman)
    TextView tvWoman;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.sp_folk)
    Spinner spFolk;
    @BindView(R.id.edt_card_id)
    EditText edtCardId;
    @BindView(R.id.sp_province)
    Spinner spProvince;
    @BindView(R.id.sp_city)
    Spinner spCity;
    @BindView(R.id.sp_district)
    Spinner spDistrict;
    @BindView(R.id.tv_household_registration)
    TextView tvHouseholdRegistration;
    @BindView(R.id.tv_unhousehold_registration)
    TextView tvUnhouseholdRegistration;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_company)
    EditText edtCompany;
    @BindView(R.id.edt_my_phone)
    EditText edtMyPhone;
    @BindView(R.id.edt_friend_phone)
    EditText edtFriendPhone;
    @BindView(R.id.sp_blood_type)
    Spinner spBloodType;
    @BindView(R.id.sp_rh)
    Spinner spRh;
    @BindView(R.id.sp_education_degree)
    Spinner spEducationDegree;
    @BindView(R.id.sp_profession)
    Spinner spProfession;
    @BindView(R.id.sp_marriage)
    Spinner spMarriage;
    @BindView(R.id.sp_pay_style)
    Spinner spPayStyle;
    @BindView(R.id.sp_medicine)
    Spinner spMedicine;
    @BindView(R.id.sp_expose)
    Spinner spExpose;
    @BindView(R.id.imgBtn_add)
    ImageButton imgBtnAdd;
    @BindView(R.id.imgBtn_add_surgery)
    ImageButton imgBtnAddSurgery;
    @BindView(R.id.imgBtn_add_trauma)
    ImageButton imgBtnAddTrauma;
    @BindView(R.id.imgBtn_add_blood_transfusion)
    ImageButton imgBtnAddBloodTransfusion;
    @BindView(R.id.imgBtn_add_father)
    ImageButton imgBtnAddFather;
    @BindView(R.id.imgBtn_add_mother)
    ImageButton imgBtnAddMother;
    @BindView(R.id.imgBtn_add_children)
    ImageButton imgBtnAddChildren;
    @BindView(R.id.et_genetic_history)
    EditText etGeneticHistory;
    @BindView(R.id.sp_canji)
    Spinner spCanji;
    @BindView(R.id.sp_exhaust)
    Spinner spExhaust;
    @BindView(R.id.sp_fuel)
    Spinner spFuel;
    @BindView(R.id.sp_water)
    Spinner spWater;
    @BindView(R.id.sp_wc)
    Spinner spWc;
    @BindView(R.id.sp_qcl)
    Spinner spQcl;
    @BindView(R.id.recyView_disease)
    RecyclerView recyViewDisease;
    @BindView(R.id.recyView_surgery)
    RecyclerView recyViewSurgery;
    @BindView(R.id.recyView_trauma)
    RecyclerView recyViewTrauma;
    @BindView(R.id.recyView_blood_transfusion)
    RecyclerView recyViewBloodTransfusion;
    @BindView(R.id.recyView_father)
    RecyclerView recyViewFather;
    @BindView(R.id.recyView_mother)
    RecyclerView recyViewMother;
    @BindView(R.id.recyView_children)
    RecyclerView recyViewChildren;
    private Unbinder unbinder;
    private ArrayAdapter spFolkAdapter = null;
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private ArrayAdapter<String> spBloodTypeAdapter = null;
    private ArrayAdapter<String> spRhTypeAdapter = null;
    private ArrayAdapter<String> spEducationDegreeAdapter = null;
    private ArrayAdapter<String> spProfessionAdapter = null;
    private ArrayAdapter<String> spMarriageAdapter = null;
    private ArrayAdapter<String> spPayStyleAdapter = null;
    private ArrayAdapter<String> spMedicineAdapter = null;
    private ArrayAdapter<String> spCanjiAdapter = null;
    private ArrayAdapter<String> spExposeAdapter = null;
    private ArrayAdapter<String> spExhaustAdapter = null;
    private ArrayAdapter<String> spFuelAdapter = null;
    private ArrayAdapter<String> spWaterAdapter = null;
    private ArrayAdapter<String> spWcAdapter = null;
    private ArrayAdapter<String> spQclAdapter = null;
    private static int provincePosition = 3;

    public HealthArchivesFragment() {
    }

    //网络获取各个控件的值
    private void setUserMessage(HealthArchivesBean.UserMessageDataBean userMessageDataBean) {
        edtName.setText(userMessageDataBean.getName());
        if (userMessageDataBean.getSex().equals("1")) {
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        } else if (userMessageDataBean.getSex().equals("2")) {
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
        } else {
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        }

        tvDate.setText(userMessageDataBean.getBirth());
        //设置民族
        String[] folk = getResources().getStringArray(R.array.nationResArray);
        for (int i = 0; i < folk.length; i++) {
            if (folk[i].equals(userMessageDataBean.getNation())) {
                spFolk.setSelection(i);
            }
        }

        //设置身份证号码
        edtCardId.setText(userMessageDataBean.getCard_id());
        //三级联动
        for (int i = 0; i < province.length; i++) {
            if (province[i].equals(userMessageDataBean.getProvince())) {
                spProvince.setSelection(i);
                for (int j = 0; j < city[i].length; j++) {
                    if (city[i][j].equals(userMessageDataBean.getCity())) {
                        spCity.setSelection(j);
                        for (int k = 0; k < county[i][j].length; k++) {
                            if (county[i][j][k].equals(userMessageDataBean.getDistrict())) {
                                spDistrict.setSelection(k);
                            }
                        }
                    }
                }
            }
        }

        //户籍
        if (userMessageDataBean.getResident().equals("0")) {
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        } else if (userMessageDataBean.getResident().equals("1")) {
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
        } else {
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        }
        //地址
        edtAddress.setText(userMessageDataBean.getAddress());
        //工作单位
        edtCompany.setText(userMessageDataBean.getWork());
        //本人电话
        edtMyPhone.setText(userMessageDataBean.getPhone());
        //常用联系人电话
        edtFriendPhone.setText(userMessageDataBean.getPhone_contacts());
        //血型
        spBloodType.setSelection(Integer.parseInt(userMessageDataBean.getBlood()) - 1);
        //RH阴性
        spRh.setSelection(Integer.parseInt(userMessageDataBean.getBlood_hr()) - 1);
        //文化程度
        spEducationDegree.setSelection(Integer.parseInt(userMessageDataBean.getEdu()) - 1);
        //职业
        spProfession.setSelection(Integer.parseInt(userMessageDataBean.getOcc()) - 1);
        //婚姻状态
        spMarriage.setSelection(Integer.parseInt(userMessageDataBean.getMarr()) - 1);
        //医疗费用支付方式
        spPayStyle.setSelection(Integer.parseInt(userMessageDataBean.getPay_type()) - 1);
        //药物过敏史
        spMedicine.setSelection(Integer.parseInt(userMessageDataBean.getAllergor()) - 1);
        //遗传病史
        etGeneticHistory.setText(userMessageDataBean.getInheri());
        //暴露史
        spExpose.setSelection(Integer.parseInt(userMessageDataBean.getExpose()) - 1);
        //残疾情况
        spCanji.setSelection(Integer.parseInt(userMessageDataBean.getDeformity()) - 1);
        //厨房排风设施
        spExhaust.setSelection(Integer.parseInt(userMessageDataBean.getExha()) - 1);
        //燃料类型
        spFuel.setSelection(Integer.parseInt(userMessageDataBean.getFuel()) - 1);
        //饮水
        spWater.setSelection(Integer.parseInt(userMessageDataBean.getWater()) - 1);
        //厕所
        spWc.setSelection(Integer.parseInt(userMessageDataBean.getWc()) - 1);
        //禽畜栏
        spQcl.setSelection(Integer.parseInt(userMessageDataBean.getPoultry()) - 1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_archives, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    //初始化各个控件
    private void initView() {
        initCustomTimePicker(tvDate);
        spFolkAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.nationResArray));
        spFolk.setAdapter(spFolkAdapter);
        spFolk.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        //三级联动
        provinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, province);
        spProvince.setAdapter(provinceAdapter);
        spProvince.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[0]);
        spCity.setAdapter(cityAdapter);
        spCity.setSelection(0, true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[0][0]);
        spDistrict.setAdapter(countyAdapter);
        spDistrict.setSelection(0, true);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[position]);
                spCity.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[provincePosition][position]);
                spDistrict.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //血型
        spBloodTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.bloods));
        spBloodType.setAdapter(spBloodTypeAdapter);
        spBloodType.setSelection(0, true);
        //RH阴性
        spRhTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.rhNegative));
        spRh.setAdapter(spRhTypeAdapter);
        spRh.setSelection(0, true);
        //文化程度
        spEducationDegreeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.educationDegrees));
        spEducationDegree.setAdapter(spEducationDegreeAdapter);
        spEducationDegree.setSelection(0, true);
        //职业
        spProfessionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.professions));
        spProfession.setAdapter(spProfessionAdapter);
        spProfession.setSelection(0, true);
        //婚姻状态
        spMarriageAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.marriage));
        spMarriage.setAdapter(spMarriageAdapter);
        spMarriage.setSelection(0, true);
        //医疗费用支付方式
        spPayStyleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.payStyles));
        spPayStyle.setAdapter(spPayStyleAdapter);
        spPayStyle.setSelection(0, true);
        //药物过敏史
        spMedicineAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.medicines));
        spMedicine.setAdapter(spMedicineAdapter);
        spMedicine.setSelection(0, true);
        //暴露史
        spExposeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.exposes));
        spExpose.setAdapter(spExposeAdapter);
        spExpose.setSelection(0, true);
        //残疾情况
        spCanjiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.canjis));
        spCanji.setAdapter(spCanjiAdapter);
        spCanji.setSelection(0, true);
        //厨房排风设施
        spExhaustAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.exhausts));
        spExhaust.setAdapter(spExhaustAdapter);
        spExhaust.setSelection(0, true);
        //燃料类型
        spFuelAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.fuels));
        spFuel.setAdapter(spFuelAdapter);
        spFuel.setSelection(0, true);
        //饮水
        spWaterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.waters));
        spWater.setAdapter(spWaterAdapter);
        spWater.setSelection(0, true);
        //厕所
        spWcAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.wcs));
        spWc.setAdapter(spWcAdapter);
        spWc.setSelection(0, true);
        //禽畜栏
        spQclAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.qcls));
        spQcl.setAdapter(spQclAdapter);
        spQcl.setSelection(0, true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                getHealthArchivesInterface();
            }
        }).start();
    }

    private void getHealthArchivesInterface() {
        Log.e(TAG, "getHealthArchivesInterface: " + InterfaceUrl.HEALTH_ARCHIVES_URL + sessonWithCode + "/m_id/" + HomeActivity.mid);
        OkHttpUtils.get().url(InterfaceUrl.HEALTH_ARCHIVES_URL + sessonWithCode)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                healthArchivesHandler.sendMessage(msg);

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_camera, R.id.tv_man, R.id.tv_woman, R.id.tv_date, R.id.tv_household_registration, R.id.tv_unhousehold_registration, R.id.imgBtn_add, R.id.imgBtn_add_surgery, R.id.imgBtn_add_trauma, R.id.imgBtn_add_blood_transfusion, R.id.imgBtn_add_father, R.id.imgBtn_add_mother, R.id.imgBtn_add_children, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_camera://拍照
                break;
            case R.id.tv_man://性别：男
                tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                break;
            case R.id.tv_woman://性别：女
                tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                break;
            case R.id.tv_date://出生日期
                timePickerView.show();
                break;
            case R.id.tv_household_registration://户籍
                tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                break;
            case R.id.tv_unhousehold_registration://非户籍
                tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                break;
            case R.id.imgBtn_add://增加疾病
                break;
            case R.id.imgBtn_add_surgery://手术
                break;
            case R.id.imgBtn_add_trauma://外伤
                break;
            case R.id.imgBtn_add_blood_transfusion://输血
                break;
            case R.id.imgBtn_add_father://家族史：父亲
                break;
            case R.id.imgBtn_add_mother://母亲
                break;
            case R.id.imgBtn_add_children://子女
                break;
            case R.id.btn_save://保存
                break;
        }
    }


    private Handler healthArchivesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthArchivesBean healthArchivesBean = GsonParsing.getHealthArchivesMessageJson(result);
                    HealthArchivesBean.UserMessageDataBean userMessageDataBean = healthArchivesBean.getData();
                    setUserMessage(userMessageDataBean);


                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    private TimePickerView timePickerView;//时间选择器

    private void initCustomTimePicker(final TextView textView) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        Calendar endDate = Calendar.getInstance();//结束时间
        startDate.set(1850, 1, 1);
        endDate.set(2200, 1, 28);
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //可根据需要自行截取数据显示在控件上  yyyy-MM-dd HH:mm:ss  或yyyy-MM-dd
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                textView.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTextColorCenter(Color.parseColor("#FF4081"))//设置选中文字的颜色#64AE4A
                .setTextColorOut(Color.parseColor("#717171"))//设置选中项以外的颜色#64AE4A
                .setLineSpacingMultiplier(2.4f)//设置两横线之间的间隔倍数
                .setDividerColor(Color.parseColor("#24E1E4"))//设置分割线的颜色
                .setDividerType(WheelView.DividerType.WRAP)//设置分割线的类型
                .setBgColor(Color.parseColor("#FFFFFF"))//背景颜色(必须是16进制) Night mode#2AA2BC
                .gravity(Gravity.CENTER)//设置控件显示位置 default is center*/
                .isDialog(true)//设置显示位置
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });
                    }
                })
                .build();
    }


}
