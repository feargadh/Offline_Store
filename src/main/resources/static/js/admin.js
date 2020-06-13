var app;
app = new Vue({
    el: '#app',
    data() {
        return {
            showUser:false,
            user:null,
            type: '9',
            Ind:0,//实时修改用户表下标
            msg:null,

            userList:null,//用户列表
            s_username:"",//用户名筛选
            s_phone:"",//电话号码筛选

            shopList:null,//门店列表
            shop_name:null,//门店名筛选
            shop_master:null,//店主筛选
            shop_address:null,//地址筛选
            errorFlag:0,

            commodityList:null,
            c_name:'',
            season:'',
            c_type:'',
            c_master:'',

            orderList:null,
            o_id:'',
            o_master:'',
            o_date:'',
            detail:null,
            detail_price:0,
            customer:'',

            usernameFlag:false,
            otherInfoFlag:false,
            securityFlag:false,

            typeList:["衬衫","短袖","长袖","马甲","夹克","风衣","裙子","短裤","长裤","卫衣","配件"],
            seasonList:["20SS","19AW","19SS","18AW","18SS"],

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
                    this.getUsers();
                    break;
                };
                case 3:{
                    this.getShop();
                    break;
                };
                case 4: {
                    this.getUsers();
                    this.getShop();
                    break;
                };
                case 5:{
                    this.getAllCommodity();
                    break;
                };
                case 6:{
                    this.gelAllOrders();
                    break;
                }
            };
            this.type = type;
        },

        //获取用户
        getUsers:function(){
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/findAllUsers",
            }).then(response => (this.userList = response.data.userList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //添加User用户
        addUser:function () {
            var formObject = {};
            var formArray =$("#addUserForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/admin/addUser",
                data: JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
            }).catch(function (error) {
                console.log(error);
            })
        },

        //重置用户密码
        resetPwd:function (id) {
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/resetPassword",
                params: {"id":id}
            }).then(function (response) {
                alert(response.data);
            }).catch(function (error) {
                alert(error);
                console.log(error);
            })
        },

        //删除用户
        deleteUser:function (id,index,role) {
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/deleteUser",
                params: {"id":id}
            }).then(function (response) {
                alert(response.data);
                if (role==2){
                    app.userList.splice(index,1);
                } else {
                    app.shopList.splice(index,1);
                }

            }).catch(function (error) {
                alert(error);
                console.log(error);
            })

        },

        //导入用户信息进表单
        updateUser:function (id,index) {
            this.Ind = index;
            console.log(this.Ind,+index);
            $("#u_id").attr("value",id);
            $("#u_username").attr("value",this.userList[index].username);
            $("#u_phone").attr("value",this.userList[index].phone);
            document.getElementById("u_sex2").value=this.userList[index].sex;
        },

        //提交更新用户数据
        submitUpdateUser:function (index) {
            console.log(index);
            var formObject = {};
            var formArray =$("#updateUserForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: "post",
                url: "/admin/updateUser",
                data: JSON.stringify(formObject)
            }).then(function (response) {
                alert(response.data);
                Vue.set(app.userList[index], 'sex', formObject['sex']);
                Vue.set(app.userList[index], 'phone', formObject['phone']);
            }).catch(function (error) {
                console.log(error);
            })
        },

        //获取门店
        getShop:function () {
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/findAllShop",
            }).then(response => (this.shopList = response.data.shopList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //点击用户名查询其所有门店
        findUserShop:function (username) {
            app.choose(3);
            this.shop_master=username;

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

        //添加门店
        addShop(){
            var formObject = {};
            var formArray =$("#addShopForm").serializeArray();
            $.each(formArray,function(i,item){
                formObject[item.name] = item.value;
            });
            //获取店主id
            var mid=app.getMid(formObject["master"]);
            console.log(mid);
            if (mid.length>0){
                this.errorFlag=0;
                formObject["mid"]=mid[0].id;
                axios({
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8',
                        "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                    },
                    method: "post",
                    url: "/admin/addShop",
                    data: JSON.stringify(formObject)
                }).then(function (response) {
                    alert(response.data);
                }).catch(function (error) {
                    console.log(error);
                })
            } else {
                this.errorFlag=1;
                alert("店主用户不存在，请输入已存在的店主用户");
            }
        },

        //获取店主id
        getMid:function(master){
            return this.userList.filter(function (user) {
                if (user.username.toString().indexOf(master)+1){
                    return user;
                } else {
                    return null;
                }
            })
        },

        //获取所有商品
        getAllCommodity:function () {
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/findAllCommodity",
            }).then(response => (this.commodityList = response.data.commodityList))//必须用=>赋值，否则数据赋值与页面渲染会同步进行，导致页面在数值赋值完成前完成渲染
                .catch(function (error) {
                    alert(error);
                    console.log(error);
                })
        },

        //获取订单信息
        gelAllOrders:function () {
            axios({
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "X-CSRF-TOKEN": "01a66a82-b736-436a-bb61-5997509a0e00"
                },
                method: 'post',
                url: "/admin/findAllOrders",
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
                url: "/admin/updateUsername",
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
                url: "/admin/updateOther",
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
                    url: "/admin/updatePassword",
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
        //用户列表筛选
        userListFilter:function () {
            let username=this.s_username;
            let phone=this.s_phone;
            if (username.length>0||phone.length>0){
                return this.userList.filter(function (user) {
                    if (
                        (username.length==0||user.username.toString().indexOf(username)+1)&&
                        (phone.length==0||user.phone.toString().indexOf(phone)+1)
                    ){
                        return user;
                    }
                })
            }else {
                return this.userList;
            }
        },

        //门店列表筛选
        shopListFilter:function () {
            let shopName=this.shop_name;
            let shopMaster=this.shop_master;
            let shopAddress=this.shop_address;

            if (shopName||shopAddress||shopMaster){
                return this.shopList.filter(function (shop) {
                    if ((shopName==null||shop.username.toString().indexOf(shopName)+1)&&
                        (shopMaster==null||shop.shopInformation.master.username.toString().indexOf(shopMaster)+1)&&
                        (shopAddress==null||shop.shopInformation.address.toString().indexOf(shopAddress)+1)){
                        return shop;
                    }
                })
            } else {
                return this.shopList;
            }

        },

        //商品列表筛选
        commodityListFilter:function () {
            let cname=this.c_name;
            let season=this.season;
            let type=this.c_type;
            let master=this.c_master;

            if (cname.length>0||season.length>0||type.length>0||master.length>0){
                return this.commodityList.filter(function (commodity) {
                    if (
                        (commodity.c_name.toString().indexOf(cname)+1||cname.length==0)&&
                        (commodity.season.toString().indexOf(season)+1||season.length==0)&&
                        (commodity.type.toString().indexOf(type)+1||type.length==0)&&
                        (commodity.master.username.toString().indexOf(master)+1||master.length==0)
                    ) {
                        return commodity;
                    }
                })
            } else {
                return this.commodityList;
            }
        },

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


        }


    }

})