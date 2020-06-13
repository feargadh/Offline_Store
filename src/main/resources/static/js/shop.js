
var app;
app = new Vue({



    el: '#app',
    data() {
        return {
            showUser:false,
            user:null,
            test:"",
            type: '9',
            Ind:0,//实时修改用户表下标
            msg:null,

            customer_name:'',
            customer_phone:'',
            customerList:null,

            activityList:[],
            activity_typeList:["衬衫","短袖","长袖","马甲","夹克","风衣","裙子","短裤","长裤","卫衣","配件"],
            activity_seasonList:["20SS","19AW","19SS","18AW","18SS"],
            activity_s_date:'',
            activity_e_date:'',
            discount_case:0,//活动方案
            case1_set:0,//折扣
            case2_set1:0,//满
            case2_set2:0,//减
            case3_set1:0,
            case3_set2:0,
            a_type:[],
            a_season:[],

            commodityList:null,
            c_Name:'',
            season:'',
            c_type:'',
            c_master:'',

            orderList:null,
            o_id:'',
            o_master:'',
            o_date:'',
            detail:null,
            customer:'',
            detail_price:0,

            shoppingList:[],
            chooseSize:'s',
            chooseShop:null,
            type5:true,
            type8:false,
            order_name:'',
            order_phone:'',
            order_customer:null,
            plan1:[],
            plan2:[],
            plan3:[],
            order_activity_id:[],

            usernameFlag:false,
            otherInfoFlag:false,
            securityFlag:false



        }
    },
    mounted(){
        var user={}
        var array=$("#userForm").serializeArray();
        $.each(array,function(i,item){
            user[item.name] = item.value;
        });
        this.user=user;
        this.showUser=true;
    },
    methods: {
        choose: function (type) {
            switch (type) {
                case 1: {
                    this.getAllCustomer();
                    break;
                };
                case 3:{
                    this.getActivity();
                    break;
                };
                case 4: {
                    break;
                };
                case 5:{
                    this.getAllCommodity();
                    break;
                };
                case 6:{
                    this.getCommodity();
                    break;
                };
                case 7:{
                    this.getlAllOrders();
                    break;
                };
                case 8:{
                    this.getAllCommodity();
                    this.getAllCustomer();
                    this.getNowActivity();
                    break;
                }
            };
            this.type = type;
        },
        //获取顾客列表
        getAllCustomer:function(){
            let id=document.getElementById("mid").value;
            console.log(id);
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/getAllCustomer",
                params: {"id":id}
            }).then(response => (this.customerList = response.data.customerList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //添加顾客
        addCustomer:function(){
            var formObject = {};
            var formArray =$("#customerForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            let id=document.getElementById("mid").value;
            formObject["id"]=id;
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/addCustomer",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
            }).catch(function (error) {
                console.log(error);
            })
        },

        //修改顾客
        updateCustomer:function(id,index){
            this.Ind=index;
            $("#customer_id").attr("value",this.customerList[index].id);
            $("#customer_name2").attr("value",this.customerList[index].customer_name);
            $("#customer_phone2").attr("value",this.customerList[index].phone);
        },

        //提交修改顾客表单
        submitUpdateCustomer:function(index){
            var formObject = {};
            var formArray =$("#updateCustomerForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/updateCustomer",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
                Vue.set(app.customerList[index], 'customer_name', formObject['customer_name']);
                Vue.set(app.customerList[index], 'phone', formObject['phone']);
            }).catch(function (error) {
                console.log(error);
            })
        },

        //获取活动
        getActivity:function(){
            let id=document.getElementById("user_id").value;
            console.log(id);
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/getAllActivities",
                params: {"id":id}
            }).then(response => (this.activityList = response.data.activityList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })

        },

        //获取正在举行的活动
        getNowActivity:function(){
            let id=document.getElementById("user_id").value;
            console.log(id);
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/getNowActivity",
                params: {"id":id}
            }).then(response => (this.activityList = response.data.activityList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })

        },

        //JS浮点小数计算不精确需要自行创建计算方法
        //乘法
        NumberMul:function(arg1, arg2) {
            var m = 0;
            var s1 = arg1.toString();
            var s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            } catch (e) {}
            try {
                m += s2.split(".")[1].length;
            } catch (e) {}

            return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
        },
        //加法
        NumberAdd:function(arg1, arg2) {
            var r1, r2, m, n;
            try {
                r1 = arg1.toString().split(".")[1].length
            } catch (e) {
                r1 = 0
            }
            try {
                r2 = arg2.toString().split(".")[1].length
            } catch (e) {
                r2 = 0
            }
            m = Math.pow(10, Math.max(r1, r2))
            n = (r1 >= r2) ? r1 : r2;
            return ((arg1 * m + arg2 * m) / m).toFixed(n);
        },
        //减法
        NumberSub :function(arg1, arg2) {
            var re1, re2, m, n;
            try {
                re1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                re1 = 0;
            }
            try {
                re2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                re2 = 0;
            }
            m = Math.pow(10, Math.max(re1, re2));
            n = (re1 >= re2) ? re1 : re2;
            return ((arg1 * m - arg2 * m) / m).toFixed(n);
        },
        //除法
        NumberDiv:function(arg1, arg2) {
            var t1 = 0,
                t2 = 0,
                r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length
            } catch (e) {}
            try {
                t2 = arg2.toString().split(".")[1].length
            } catch (e) {}
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return (r1 / r2) * Math.pow(10, t2 - t1);
        },

        //计算折扣
        discountNumber:function(discount){
            console.log(discount);
            discount=math.bignumber(discount);
            console.log(discount);
            let result=100;
            console.log(result);
            result=math.number(math.multiply(result,discount));
            console.log(result);
            return result;
        },


        //添加活动
        addActivity:function(){
            if (this.case1_set>=100 ||(this.case2_set2>=this.case2_set1&&this.case2_set1!=0)){
                alert("请输入合理的优惠参数");
            } else {
                var formObject = {};
                var formArray =$("#addActivityForm").serializeArray();
                $.each(formArray,function(i,item){
                    formObject[item.name] = item.value;
                });
                let type=this.checkBoxJson(this.a_type);
                let season=this.checkBoxJson(this.a_season);
                formObject["type"] = type;
                formObject["season"] = season;

                axios({
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8',
                        "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                    },
                    method: "post",
                    url: "/shop/addActivity",
                    data:JSON.stringify(formObject)
                }).then(function (response) {
                    alert(response.data);
                }).catch(function (error) {
                    console.log(error);
                })
            }
        },

        //打包复选框
        checkBoxJson:function(arr){
            if(!arr.length) return null;

            var len = arr.length, array = [];
            for( var i=0;i<len;i++){
                array.push(arr[i]);
            }
            return JSON.stringify(array);


        },

        //删除活动
        deleteActivity:function(id,index){
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/deleteActivity",
                params: {"id":id}
            }).then(function (response) {
                alert(response.data);
                app.activityList.splice(index,1);
            }).catch(function (error) {
                alert(error);
                console.log(error);

            })

        },


        //获取所有商品
        getAllCommodity:function () {
            let id=document.getElementById("user_id").value;
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/findAllCommodity",
                params: {"id":id},
            }).then(response => (this.commodityList = response.data.commodityList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //根据USER_id获得商品款式
        getCommodity:function(){
            let id=document.getElementById("mid").value;
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/findCommodityByMid",
                params: {"id":id},
            }).then(response => (this.commodityList = response.data.commodityList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //商品入库
        addCommodityInfo: function(){
            var formObject = {};
            var formArray =$("#addCommodityInfo").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/addCommodityInfo",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
            }).catch(function (error) {
                console.log(error);
            })
        },

        //获取订单信息
        getlAllOrders:function () {
            let id=document.getElementById("user_id").value;
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/shop/findAllOrders",
                params: {"id":id},
            }).then(response => (this.orderList = response.data.orderList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //获取订单详情
        getDetail:function (index) {
            this.detail_price=0;
            this.detail=this.orderList[index];
            this.commodityList=this.orderList[index].commodityList;
            for (let i=0;i<this.commodityList.length;i++){
                this.detail_price=this.NumberAdd(this.detail_price,this.NumberMul(this.commodityList[i].price,this.commodityList[i].quantity));
            }
            this.activityList=this.orderList[index].activityList;
        },

        //查看顾客订单
        getOrderByCustomer:function(index){
            this.customer=this.customerList[index].name;
            this.choose(7);

        },



        //导入商品信息
        addShoppingCart:function(index){
            this.chooseShop=this.commodityList[index];
        },

        //加入订单
        addOrderInfo:function () {
            var formObject = {};
            var formArray =$("#orderInfo").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            console.log(JSON.stringify(formObject));
            this.shoppingList.push(JSON.parse(JSON.stringify(formObject)));

        },
        //移出购物车
        removeShop:function (index) {
            this.shoppingList.splice(index,1);
        },

        //计算优惠后的价格
        discountPrice:function (price,discount) {
                return this.NumberMul(price,discount);

        },

        //单体最优优惠计算
        singleDiscount:function (shopping) {
            let memberDiscount=1;//会员折扣
            let activity_id=0;

            if (app.customerListFilter3.length>0){
                memberDiscount=app.customerListFilter3[0].membership;
                console.log(app.customerListFilter3[0].customer_name+" : "+memberDiscount);
            }

            let result=this.NumberMul(shopping.price,memberDiscount);

            //计算最优折扣优惠
            if (app.activityListFilter3.length>0){
                for (let i=0;i<app.activityListFilter3.length;i++){
                    let  activity=app.activityListFilter3[i];
                    if (
                        (activity.type.toString().indexOf(shopping.type)+1||activity.type.toString().length==0)&&
                        (activity.season.toString().indexOf(shopping.season)+1||activity.season.toString().length==0)
                    ) {
                        let price=math.multiply(shopping.price,activity.discount);
                        if (activity.overlay.toString().indexOf("1")+1){
                            price=math.multiply(price,memberDiscount);
                        }
                        if (result>price){
                            result = price;
                        }
                    }
                }
            }


            if (app.activityListFilter4.length>0){
                for (let i=0;i<app.activityListFilter4.length;i++){
                    let  activity=app.activityListFilter4[i];
                    if (
                        (activity.type.toString().indexOf(shopping.type)+1||activity.type.toString().length==0)&&
                        (activity.season.toString().indexOf(shopping.season)+1||activity.season.toString().length==0)
                    ){
                        let price=this.NumberMul(shopping.price,this.getDiscountDouble(activity.discount));
                        if (activity.overlay.toString().indexOf("1")+1){
                            price=this.NumberMul(price,memberDiscount);
                        }
                        if (result>price){
                            result = price;
                        }
                    }
                }
            }

            result=this.NumberDiv(Math.floor(this.NumberMul(result,100)),100);

            return result;


            
        },

        //将折扣统一取小数部分
        getDiscountDouble:function(discount){
          if (discount<1){
              return discount;
          }else {
              return this.NumberSub(discount,Math.floor(discount));
          }
        },

        //若当前购物车是否符合满折活动条件
        checkShoppingActivity:function (activity) {
            let t=activity.type.toString();//获取参与活动类型
            let s=activity.season.toString();//获取参与活动季度
            let full=Math.floor(activity.discount);//满足数量
            let number=0;
            for (let i=0;i<this.shoppingList.length;i++){
                if ((t.indexOf(this.shoppingList[i].type)+1 || t.length==0) &&
                    (s.indexOf(this.shoppingList[i].season)+1||s.length==0)
                ) {
                    number=this.NumberAdd(number,this.shoppingList[i].quantity);
                    console.log("full: "+full+" number:"+number)
                    if (number>=full){

                        return true;
                    }
                }
            }
            return false;
        },

        //计算满减前的总价
        AfterFullCutTotal:function(){
            let list=app.shoppingListFilter;
            let result=0;
            if (list.length>0){
                for (let i=0;i<list.length;i++){
                    result=app.NumberAdd(result,app.NumberMul(app.singleDiscount(list[i]),list[i].quantity));
                }
            }

            return result;
        },

        //总价进行满减
        TotalFullCut:function (total) {

            let result=total;

            let activity1=app.activityListFilter5;
            //先计算可叠加优惠
            if (activity1.length>0){
                for ( let i=0;i<activity1.length;i++){
                    let full=Math.floor(activity1[i].discount);
                    let cut=this.NumberMul(this.NumberSub(activity1[i].discount,full),full);
                    let times=Math.floor(this.NumberDiv(total,full));
                    let price=this.NumberSub(total,this.NumberMul(times,cut));

                    console.log("满:"+full+" 减"+cut+" price"+price);

                    if (result>price){
                        result=price;
                    }
                }
            }

            //计算单次满减
            let activity2=app.activityListFilter6;
            if (activity2.length>0){
                for (let i=0;i<activity2.length;i++){
                    let full=Math.floor(activity2[i].discount);
                    let cut=this.NumberMul(this.NumberSub(activity2[i].discount,full),full);
                    let price=this.NumberSub(result,cut);

                    if (result>price){
                        result=price;
                    }
                }
            }
            return result;
        },

        //创建订单
        addOrder:function () {
            if (this.order_name.length==0||this.order_phone.length==0){
                alert("请输入顾客姓名以及联系方式");
            } else {
                var formObject = {};
                var formArray =$("#createOrderForm").serializeArray();
                $.each(formArray,function(i,item){
                    formObject[item.name] = item.value;
                });

                //如果顾客不存在则，先添加顾客

                    formObject["shoppingList"]=JSON.stringify(this.shoppingList);


                    //打包活动id
                    let array=[];
                    if (app.activityListFilter3.length>0){
                        for (let i=0;i<app.activityListFilter3.length;i++){
                            array.push(app.activityListFilter3[i].id)
                        }
                    }
                    if (app.activityListFilter4.length>0){
                        for (let i=0;i<app.activityListFilter4.length;i++){
                            array.push(app.activityListFilter4[i].id)
                        }
                    }
                    if (app.activityListFilter5.length>0){
                        for (let i=0;i<app.activityListFilter5.length;i++){
                            array.push(app.activityListFilter5[i].id)
                        }
                    }
                    if (app.activityListFilter6.length>0){
                        for (let i=0;i<app.activityListFilter6.length;i++){
                            array.push(app.activityListFilter6[i].id)
                        }
                    }
                    formObject["activity.id"]=JSON.stringify(array);

                    console.log(JSON.stringify(formObject));

                if (app.customerListFilter3.length==0) {
                    var customer = {}
                    customer["customer_name"] = this.order_name;
                    customer["phone"] = this.order_phone;
                    let id = document.getElementById("mid").value;
                    customer["id"] = id;
                    axios({
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8',
                            "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                        },
                        method: "post",
                        url: "/shop/addCustomer",
                        data: JSON.stringify(customer)
                    }).then(function (response) {
                        console.log(response.data);
                        formObject["cus_id"]=response.data;
                        console.log("cus_id"+formObject["cus_id"])
                        app.addOrderAction(formObject);
                    }).catch(function (error) {
                        console.log(error);

                    })
                }else {
                    formObject["cus_id"]=app.customerListFilter3[0].id;
                    console.log("cus_id"+formObject["cus_id"])
                    app.addOrderAction(formObject);
                }
            }
        },


        //创建订单操作
        addOrderAction:function(formObject){
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/addOrder",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
                app.clearShoppingList();
            }).catch(function (error) {
                console.log(error);
            })
        },

        //清空购物车
        clearShoppingList:function () {
            this.shoppingList.splice(0,this.shoppingList.length);
        },

        //修改用户名
        editUsernameForm:function () {
          this.usernameFlag=!this.usernameFlag;
        },
        updateUsername:function () {
            var formObject = {};
            var formArray =$("#updateUsernameForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/updateUsername",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
                Vue.set(app.user, 'username', formObject['username']);
                this.editUsernameForm();

            }).catch(function (error) {
                console.log(error);
            })
        },
        //修改其他信息
        editOther:function () {
            this.otherInfoFlag=!this.otherInfoFlag;
        },
        updateOther:function () {
            var formObject = {};
            var formArray =$("#otherInfoForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/shop/updateOther",
                data:JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
                Vue.set(app.user, 'sex', formObject['sex']);
                Vue.set(app.user, 'phone', formObject['phone']);
                this.editOther();
            }).catch(function (error) {
                console.log(error);
            })
        },
        //修改密码
        editPassword:function () {
            this.securityFlag=!this.securityFlag;
        },
        updatePassword:function(){
            var formObject={};
            var formArray =$("#updatePasswordForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            if (formObject["password"].indexOf(formObject["re_password"])+1){
                axios({
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8',
                        "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                    },
                    method: "post",
                    url: "/shop/updatePassword",
                    data:JSON.stringify(formObject)
                }).then(function (response) {
                    alert(response.data);
                    this.editPassword();
                }).catch(function (error) {
                    console.log(error);
                })
            }else {
                alert("两次密码不一致");
            }

        },


    },
    computed: {

        //顾客筛选
        customerListFilter:function(){
            let name=this.customer_name;
            let phone=this.customer_phone;

            if (name.length>0||phone.length>0){
                return this.customerList.filter(function (customer) {
                    if (
                        (customer.customer_name.toString().indexOf(name)+1||name.length==0)&&
                        (customer.phone.toString().indexOf(phone)+1||phone.length==0)
                    ) {
                        return customer;
                    }
                })
            } else {
                return this.customerList;
            }
        },

        //活动筛选
        activityListFilter:function(){
            let start_date=this.activity_s_date;
            let end_date=this.activity_e_date;

            if (start_date.length>0||end_date.length>0){
                return this.activityList.filter(function (activity) {
                    if (
                        (start_date.length==0||activity.start_date.toString().indexOf(start_date)+1)&&
                        (end_date.length==0||activity.end_date.toString().indexOf(end_date)+1)
                    ){
                        return activity;
                    }
                })
            }else {
                return this.activityList;
            }

        },

        //商品列表筛选
        commodityListFilter:function () {
            let cname=this.c_Name;
            let season=this.season;
            let type=this.c_type;

            if (cname.length>0||season.length>0||type.length>0){
                return this.commodityList.filter(function (commodity) {
                    if (
                        (commodity.commodity.c_name.toString().indexOf(cname)+1||cname.length==0)&&
                        (commodity.commodity.season.toString().indexOf(season)+1||season.length==0)&&
                        (commodity.commodity.type.toString().indexOf(type)+1||type.length==0)
                    ) {
                        return commodity;
                    }
                })
            } else {
                return this.commodityList;
            }
        },


        //订单筛选
        orderListFilter:function () {
            let master=this.o_master;
            let id=this.o_id;
            let date=this.o_date;
            let customer=this.customer;

            if (master.length>0||id.length>0||date.length>0||customer.length>0){
                return  this.orderList.filter(function (order) {
                    if (
                        (master.length==0||order.master.username.toString().indexOf(master)+1)&&
                        (id.length==0||order.id.toString().indexOf(id)+1)&&
                        (date.length==0||order.date.toString().indexOf(date)+1)&&
                        (customer.length==0||order.customer.name.toString().indexOf(customer)+1)
                    ){
                        return order;
                    }
                })
            }else {
                return this.orderList;
            }


        },

        //动态控制type5 8的CSS
        type8Watch:function () {
            if (this.type==8){
                this.type8=true;
            }else {
                this.type8=false;
            }
            return{
                container:this.type5,
                shop8:this.type8
            }
        },

        //订单顾客筛选
        customerListFilter2:function () {
            let name=this.order_name;
            let phone=this.order_phone;

            if (name.length>0||phone.length>0){
                return this.customerList.filter(function (customer) {
                    if (
                        (customer.customer_name.toString().indexOf(name)+1||name.length==0)&&
                        (customer.phone.toString().indexOf(phone)+1||phone.length==0)
                    ){
                        return customer;
                    }
                })
            } else {
                return this.customerList;
            }
        },

        //购物车调整
        shoppingListFilter:function () {

            return this.shoppingList.filter(function (shopping) {

                console.log(shopping.commodityName+": "+app.singleDiscount(shopping));

                return shopping;
            })
        } ,

        //订单总价
        orderTotalPrice:function () {
           let result=0;

           result=app.AfterFullCutTotal();

           console.log("未计算满减:"+result);

           //计算满减;
            result=app.TotalFullCut(result);
            console.log("计算满减："+result);

            return result;
        },

        //匹配出订单顾客
        customerListFilter3:function () {
            let customerName=this.order_name;
            let customerPhone=this.order_phone;

            return this.customerList.filter(function (customer) {
                if (customerName.length>0&&customerPhone.length>0) {
                    if (customer.customer_name.toString().indexOf(customerName) + 1 && customer.phone.toString().indexOf(customerPhone) + 1) {
                        return customer;
                    }
                }
            })

        },

        activityListFilter3:function () {
            return this.activityList.filter(function (activity) {
                if (activity.plan.toString().indexOf("1")+1){
                    for (let i=0;i<app.shoppingList.length;i++){
                        if ((activity.type.toString().indexOf(app.shoppingList[i].type)+1||activity.type.toString().length==0)&&
                            (activity.season.toString().indexOf(app.shoppingList[i].season)+1||activity.season.toString().length==0)){
                            return activity;
                        }

                    }
                }
            })
        },

        //活动类型3筛选
        activityListFilter4:function () {
            return this.activityList.filter(function (activity) {
                if (activity.plan.toString().indexOf("3")+1&&app.checkShoppingActivity(activity)){
                    return activity;
                }
            })
        },

        //活动类型2筛选
        activityListFilter5:function () {
            return this.activityList.filter(function (activity) {
                if (activity.plan.toString().indexOf("2")+1&&
                    activity.overlay.toString().indexOf("1")+1){

                    console.log("filter5:"+ app.AfterFullCutTotal()+"  "+Math.floor(activity.discount))

                    if (app.AfterFullCutTotal()>Math.floor(activity.discount)) {
                        return activity;
                    }
                }
            })
        },
        activityListFilter6:function () {
            return this.activityList.filter(function (activity) {
                if (activity.plan.toString().indexOf("2")+1&&
                    activity.overlay.toString().indexOf("0")+1){
                    console.log("filter6:"+ app.AfterFullCutTotal()+"  "+Math.floor(activity.discount))

                    if (app.AfterFullCutTotal()>Math.floor(activity.discount)) {
                        return activity;
                    }
                }
            })
        },



    }

})

