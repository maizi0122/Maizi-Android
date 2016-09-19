# Ripened (the awn of wheat)
The open-source project of Maizi-Studio

Module Ripened -- a light-weight automatic view injection and smart listener、resources binding library for android(it is just a beginning...)

  this project's main module is Ripened, modules: adapter_enhance、contentview_enhance、eventbinder_enhance、resourcebinder_enhance

  are the plugin of Ripened, we are support free-choosing of composing, so you can add module with the function just you need.

  it can minify your apk, we also support your customize plugin with implementation of IPlugin interface...

  1.download the source-bundle at the right of the webpage.

  2.import this library into your gradle project.

  3.edit your app module with build.gradle like this:

  dependencies {

      //-----add library module like this only for view injection-----
      compile project(':Ripened') //it's the base-common module of it's plugin.

      //-----add library module like this for view injection and content-view setting-----
      compile project(':contentview_enhance') //it's an plugin of module Ripened with enhancing of content-view setting

      //-----add library module like this for view injection and adapter view setting-----
      compile project(':adapter_enhance') //it's an plugin of module Ripened with enhancing of adapter setting

      //-----add library module like this for view injection and listener binding-----
      compile project(':eventbinder_enhance') //it's an plugin of module Ripened with enhancing of listener binding

      //-----seeing the demo for more detail...

  4.1 function of auto view injection:

      edit your subclass of Activity like this:

      public class MainActivity extends Activity {

          //--and add annotation like this--
          @ResId(id = R.id.ac_main_bt1)
          private Button ac_main_bt1;
          //--define your field of view with the name you like--

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              setContentView(R.layout.activity_main);
              //--invoke auto view injection like this--
              new Ripened().inject(this);
              //why not static-method? it will be product by the bean-factory of maizi-studio open source project at soon.like spring...
          }
      }

      edit your subclass of Fragment like this:
      public class Fragment_Maizi extends android.app.Fragment {

          @ResId(id = R.id.ac_main_frag1_bt1)
          private Button ac_main_frag1_bt1;

          public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                  View root = inflater.inflate(R.layout.layout_fragment_1, container, false);
                  //--invoke auto view injection like this-
                  new Ripened().inject(this, root);
                  //---------------------------------------
                  return root;
              }
      }

      edit your adapterView activity like this:
      public class AdapterViewActivity1 extends Activity {

          private List<String> list = new ArrayList<String>() {
              {
                  for (int i = 0; i < 20; i++) {
                      this.add("(<-press img)...hello maizi...(<-long press tv)" + i);
                  }
              }
          };

          @ResId(R.id.ac_sec_lv)
          @Adapter(MyAdapter.class)
          private ListView ac_sec_lv;

          private IRipened ripened;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              //you adapter have no-params constructor,so if you config @Adapter(MyAdapter.class) at right place,we'll make instance automatic and inject it.
              ripened = new Ripened().inject(this);

          }

          private class MyAdapter extends BaseAdapter implements View.OnLongClickListener {

              /*private MyAdapter(){} default empty-params constructor*/

              @Override
              public int getCount() {
                  return list.size();
              }

              @Override
              public Object getItem(int position) {
                  return null;
              }

              @Override
              public long getItemId(int position) {
                  return 0;
              }

              @Override
              public View getView(int position, View convertView, ViewGroup parent) {
                  View itemView = null;
                  MyHolder holder = null;
                  if (convertView != null) {
                      itemView = convertView;
                      holder = MyHolder.class.cast(itemView.getTag());
                  } else {
                      itemView = LayoutInflater.from(AdapterViewActivity1.this).inflate(R.layout.ac_sec_lv_item, parent, false);
                      holder = new MyHolder();
                      //-------------------------------------------------
                      ripened.inject(this, itemView, holder);
                      //-------------------------------------------------
                      holder.ac_sec_lv_item_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                      itemView.setTag(holder);
                  }
                  holder.ac_sec_lv_item_iv.setImageResource(R.mipmap.ic_launcher);
                  holder.ac_sec_lv_item_tv.setText(list.get(position));
                  return itemView;
              }

              private class MyHolder {

                  @ResId(R.id.ac_sec_lv_item_iv)
                  ImageView ac_sec_lv_item_iv;

                  @ResId(R.id.ac_sec_lv_item_tv)
                  TextView ac_sec_lv_item_tv;

                  @ResId(R.id.ac_sec_lv_item_root)
                  View itemView;
              }

          }
      }


  4.2 function of smart listener binding.

      if you abide by the rule of naming the set-method for one field of your class, which follows with the rule of android-source project.<br />
      when your field's type is an interface which have only one method, your set-method should be named like setXxxYyy<br />
      when your field's type is an interface which have more than one method, your set-method should be named like addXxxYyy<br />
      such as:
      public class A{
          private Xxx abc;

          public void setAbc(Xxx abc){
                this.abc = abc;
            }
          //the interface which has only one function(),it's "set-method" start with "set"
          public interface Xxx{
                return-type fun(params...);
          }

          private Yyy def;

          public void addDef(Yyy def){
                this.def = def;
          }
          //the interface which has more than one function(),it's "set-method" start with "add"
          public interface Yyy{
                return-type fun1(params...);
                return-type fun2(params...);
                .
                .
          }
      }

      if you setup this object, we can help you bind the listener smartly.

      In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.

      we have many ways to bind the listener.example below:

      **********************************************************************************************

      public class CustomOnClickListener1 implements View.OnClickListener {

          @Override
          @EventTarget(targets = {R.id.ac_main_bt5,R.id.ac_main_frag1_bt5})
          public void onClick(View v) {
              Toast.makeText(v.getContext(), "...fifth way...\nhello maizi", Toast.LENGTH_SHORT).show();
          }

      }
      ============================================================================================
      public class CustomOnClickListener2 implements View.OnClickListener {

          private String text;

          public CustomOnClickListener2(String text) {
              this.text = text;
          }

          @Override
          @EventTarget(targets = {R.id.ac_main_bt6, R.id.ac_main_frag1_bt6})
          public void onClick(View v) {
              Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
          }
      }

      ************************************Activity like this****************************************

      edit your subclass of Activity like this:

      @ContentView(R.layout.activity_main)
      public class MainActivity extends Activity implements View.OnClickListener {

          @ResId(R.id.ac_main_bt1)
          @RegistListener(listeners = {MainActivity.class})
          //auto animation resources binding.
          @Anim(animResId = R.anim.slide_in_left, duration = 1000, startOffset = 0, interpolator = android.R.interpolator.linear, repeatCount = 1, fillAfter = true)
          private Button ac_main_bt1;

          @ResId(R.id.ac_main_bt2)
          @RegistListener(listeners = {MyOnClickListener2.class})
          @Anim(animResId = R.anim.slide_in_right, duration = 1000)
          private Button ac_main_bt2;

          @ResId(R.id.ac_main_bt3)
          @RegistListener(listeners = {MyOnClickListener3.class})
          @Anim(animResId = R.anim.slide_in_top, duration = 1000)
          private Button ac_main_bt3;

          @ResId(R.id.ac_main_bt4)
          @RegistListener(listeners = {MyOnClickListener4.class})
          @Anim(animResId = R.anim.slide_in_top_self, duration = 1000)
          private Button ac_main_bt4;

          @ResId(R.id.ac_main_bt5)
          @Anim(animResId = R.anim.slide_in_bottom_self, duration = 1000)
          @RegistListener(listeners = {CustomOnClickListener1.class})
          private Button ac_main_bt5;

          @ResId(R.id.ac_main_bt6)
          @Anim(animResId = R.anim.slide_in_bottom, duration = 1000)
          @RegistListener(listeners = {CustomOnClickListener2.class})
          private Button ac_main_bt6;

          @ResId(R.id.ac_main_bt7)
          @Anim(animResId = R.anim.fade_in, duration = 1000, interpolator = android.R.interpolator.accelerate_quad)
          private Button ac_main_bt7;

          @ResId(R.id.maizi_contaniner)
          private RelativeLayout maizi_contaniner;


          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              //--invoke auto view injection like this--      you can choose which one or more plugin to use.
              new Ripened().setVIContext(new VIContext().addPlugin(new ContentSetter(),
                                                                  new EventBinder(),
                                                                  new AnimationSetter()))
                          .inject(this,
                                  new MyOnClickListener4("...fourth way...\nhello maizi"),
                                  new CustomOnClickListener2("...sixth way...\nhello maizi"));

              //------replace upon, you can also code like this:       new Ripened().inject(this,
              //------because we will scan automatic, but we                               new MyOnClickListener4("...fourth way...\nhello maizi"),
              //------also support your custom                                             new CustomOnClickListener2("...sixth way...\nhello maizi"));

              ac_main_bt7.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Toast.makeText(MainActivity.this, "...seventh way...\nhello maizi", Toast.LENGTH_SHORT).show();
                  }
              });

              FragmentManager fragmentManager = getFragmentManager();
              FragmentTransaction ft = fragmentManager.beginTransaction();
              ft.replace(R.id.maizi_contaniner, new Fragment_Maizi());
              ft.commit();
          }

          @Override
          @EventTarget(targets = {R.id.ac_main_bt1})
          public void onClick(View v) {
              Toast.makeText(this, "...first way...\nhello maizi", Toast.LENGTH_SHORT).show();
          }

          private class MyOnClickListener2 implements View.OnClickListener {

              @Override
              @EventTarget(targets = {R.id.ac_main_bt2})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, "...second way...\nhello maizi", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener3 extends CustomOnClickListener1 {

              @Override
              @EventTarget(targets = {R.id.ac_main_bt3})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, "...third way...\nhello maizi", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener4 implements View.OnClickListener {

              private String text;

              public MyOnClickListener4(String text) {
                  this.text = text;
              }

              @Override
              @EventTarget(targets = {R.id.ac_main_bt4})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
              }
          }
      }

      **************************************fragment like this**************************************

      public class Fragment_Maizi extends android.app.Fragment implements View.OnClickListener {

          @ResId(R.id.ac_main_frag1_bt1)
          @RegistListener(listeners = {Fragment_Maizi.class})
          private Button ac_main_frag1_bt1;

          @ResId(R.id.ac_main_frag1_bt2)
          @RegistListener(listeners = {MyOnClickListener2.class})
          private Button ac_main_frag1_bt2;

          @ResId(R.id.ac_main_frag1_bt3)
          @RegistListener(listeners = {MyOnClickListener3.class})
          private Button ac_main_frag1_bt3;

          @ResId(R.id.ac_main_frag1_bt4)
          @RegistListener(listeners = {MyOnClickListener4.class})
          private Button ac_main_frag1_bt4;

          @ResId(R.id.ac_main_frag1_bt5)
          @RegistListener(listeners = {CustomOnClickListener1.class})
          private Button ac_main_frag1_bt5;

          @ResId(R.id.ac_main_frag1_bt6)
          @RegistListener(listeners = {CustomOnClickListener2.class})
          private Button ac_main_frag1_bt6;

          @ResId(R.id.ac_main_frag1_bt7)
          private Button ac_main_frag1_bt7;

          @ResId(R.id.ac_main_frag1_bt8)
          @RegistListener(listeners = {Fragment_Maizi.class})
          private Button ac_main_frag1_bt8;

          @ResId(R.id.ac_main_frag1_bt9)
          @RegistListener(listeners = {Fragment_Maizi.class})
          private Button ac_main_frag1_bt9;

          @ResId(R.id.ac_main_frag1_bt10)
          @RegistListener(listeners = {Fragment_Maizi.class})
          private Button ac_main_frag1_bt10;

          @ResId(R.id.ac_main_frag_root)
          @Anim(animResId = R.anim.slide_in_bottom_self, duration = 2000, interpolator = android.R.interpolator.accelerate_quad)
          private RelativeLayout ac_main_frag_root;

          @Override
          public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
              View root = inflater.inflate(R.layout.layout_fragment, container, false);
              //-------------------------------------------------
              new Ripened().inject(this, root,new MyOnClickListener4("fragment\n...fourth way...\nhello maizi"),
                                             new CustomOnClickListener2("fragment\n...sixth way...\nhello maizi"));
              //-------------------------------------------------
              //you also can bind by yourself.
              ac_main_frag1_bt7.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...seventh way...\nhello maizi", Toast.LENGTH_SHORT).show();
                  }
              });
              return root;
          }

          @Override
          @EventTarget(targets = {R.id.ac_main_frag1_bt1, R.id.ac_main_frag1_bt8, R.id.ac_main_frag1_bt9, R.id.ac_main_frag1_bt10})
          public void onClick(View v) {
              switch (v.getId()) {
                  case R.id.ac_main_frag1_bt1:
                      Toast.makeText(this.getActivity(), "fragment\n...first way...\nhello maizi", Toast.LENGTH_SHORT).show();
                      break;
                  case R.id.ac_main_frag1_bt8:
                      getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity1.class));
                      break;
                  case R.id.ac_main_frag1_bt9:
                      getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity2.class));
                      break;
                  case R.id.ac_main_frag1_bt10:
                      getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity3.class));
                      break;
              }
          }

          private class MyOnClickListener2 implements View.OnClickListener {

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt2})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...second way...\nhello maizi", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener3 extends CustomOnClickListener1 {

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt3})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...third way...\nhello maizi", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener4 implements View.OnClickListener {

              private String text;

              public MyOnClickListener4(String text) {
                  this.text = text;
              }

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt4})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), text, Toast.LENGTH_SHORT).show();
              }
          }
      }

      ********************************adapter view activity like this*******************************

      @ContentView(R.layout.activity_sec)
      public class AdapterViewActivity1 extends Activity implements AdapterView.OnItemClickListener {

          private List<String> list = new ArrayList<String>() {
              {
                  for (int i = 0; i < 20; i++) {
                      this.add("(<-press img)...hello maizi...(<-long press tv)" + i);
                  }
              }
          };

          @ResId(R.id.ac_sec_lv)
          @Adapter(MyAdapter.class)
          @RegistListener(listeners = {AdapterViewActivity1.class})
          private ListView ac_sec_lv;

          @ResId(R.id.ac_ava1_root)
          @Anim(duration = 1000,interpolator = android.R.interpolator.bounce)
          private RelativeLayout ac_ava1_root;

          private IRipened ripened;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              //you adapter have no-params constructor,so if you config @Adapter(MyAdapter.class) at right place,we'll make instance automatic and inject it.
              ripened = new Ripened().inject(this);  //if your Adapter class have no empty-param constructor,you should pass the instance in
          }                                        //inject(Object... obj) manually,because we don't know what object in params to create instance...

          @Override
          @EventTarget(targets = {R.id.ac_sec_lv})
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String[] arrays = TextView.class.cast(view.findViewById(R.id.ac_sec_lv_item_tv)).getText().toString().split("\\(<\\-([a-z[\\p{Space}]]+)\\)");
              Toast.makeText(this, new StringBuilder("item ").append(arrays[1]).append(arrays[2]).append("\n have been clicked").toString(), Toast.LENGTH_SHORT).show();
          }

          private class MyAdapter extends BaseAdapter implements View.OnLongClickListener {

              /*private MyAdapter(){} default empty-params constructor*/

              @Override
              public int getCount() {
                  return list.size();
              }

              @Override
              public Object getItem(int position) {
                  return null;
              }

              @Override
              public long getItemId(int position) {
                  return 0;
              }

              @Override
              public View getView(int position, View convertView, ViewGroup parent) {
                  View itemView = null;
                  MyHolder holder = null;
                  if (convertView != null) {
                      itemView = convertView;
                      holder = MyHolder.class.cast(itemView.getTag());
                  } else {
                      itemView = LayoutInflater.from(AdapterViewActivity1.this).inflate(R.layout.ac_sec_lv_item, parent, false);
                      holder = new MyHolder();
                      //-------------------------------------------------
                      ripened.inject(this, itemView, holder);
                      //-------------------------------------------------
                      holder.ac_sec_lv_item_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                      itemView.setTag(holder);
                  }
                  holder.ac_sec_lv_item_iv.setImageResource(R.mipmap.ic_launcher);
                  holder.ac_sec_lv_item_tv.setText(list.get(position));
                  return itemView;
              }

              @Override
              @EventTarget(targets = {R.id.ac_sec_lv_item_tv})
              public boolean onLongClick(View v) {
                  String[] arrays = TextView.class.cast(v.findViewById(R.id.ac_sec_lv_item_tv)).getText().toString().split("\\(<\\-([a-z[\\p{Space}]]+)\\)");
                  Toast.makeText(AdapterViewActivity1.this, new StringBuilder("TextView ").append(arrays[1]).append(arrays[2]).append("\n have been long clicked").toString(), Toast.LENGTH_SHORT).show();
                  return false;
              }

              private class MyHolder {

                  @ResId(R.id.ac_sec_lv_item_iv)
                  ImageView ac_sec_lv_item_iv;

                  @ResId(R.id.ac_sec_lv_item_tv)
                  @RegistListener(listeners = {MyAdapter.class})
                  TextView ac_sec_lv_item_tv;

                  @ResId(R.id.ac_sec_lv_item_root)
                  View itemView;
              }

          }
      }

  5.enjoy your coding-time...
    <br/>If you want to know more about me, you can go to my blog [ <a herf="https://maizi0122.github.io/">https://maizi0122.github.io/</a> ].
    <br/>Thanks for your attention.
