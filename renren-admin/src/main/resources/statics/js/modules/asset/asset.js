$(function () {


    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/asset/list',
        datatype: "json",
        colModel: [
            { label: '资产状态', name: 'assetStatus', index: 'asset_status', width: 80, formatter: function(value, options, row){
                    if(value === 0)
                        return '<span class="label label-info">闲置</span>';
                    else if(value === 1)
                        return '<span class="label label-success">在用</span>';
                    else if(value === 2)
                        return '<span class="label label-primary">借用</span>';
                    else if(value === 3)
                        return '<span class="label label-danger">维修中</span>';
                    else if(value === 4)
                        return '<span class="label label-default">报废</span>';
                    else if(value === 5)
                        return '<span class="label label-warning">待审批</span>';
                } },
			//{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '资产编码', name: 'assetCode', index: 'asset_code', width: 80 },
			{ label: '资产名称', name: 'assetName', index: 'asset_name', width: 80 },
			//{ label: '资产分类id', name: 'categoryId', index: 'category_id', width: 80 },
			{ label: '资产分类名称', name: 'categoryName', index: 'category_name', width: 80 },
			//{ label: '所属组织id', name: 'orgId', index: 'org_id', width: 80 },
			{ label: '所属组织名称', name: 'orgName', index: 'org_name', width: 80 },
			//{ label: '使用组织id', name: 'useOrgId', index: 'use_org_id', width: 80 },
			{ label: '使用组织名称', name: 'useOrgName', index: 'use_org_name', width: 80 },
			//{ label: '使用人id, 从使用组织里面去查找', name: 'empId', index: 'emp_id', width: 80 },
			{ label: '使用人名称', name: 'empName', index: 'emp_name', width: 80 },
			//{ label: '管理员id', name: 'adminUserid', index: 'admin_userid', width: 80 },
			{ label: '管理员名称', name: 'adminUsername', index: 'admin_username', width: 80 },
			//{ label: '区域id', name: 'areaId', index: 'area_id', width: 80 },
			{ label: '区域名称', name: 'areaName', index: 'area_name', width: 80 },
			//{ label: '存放地点', name: 'storagePosition', index: 'storage_position', width: 80 },
			//{ label: '规格型号', name: 'standard', index: 'standard', width: 80 },
			//{ label: '计量单位', name: 'unit', index: 'unit', width: 80 },
			//{ label: '价值（元）', name: 'worth', index: 'worth', width: 80 },
			//{ label: '使用期限（月）', name: 'timeLimit', index: 'time_limit', width: 80 },
			{ label: '购买日期', name: 'buyTime', index: 'buy_time', width: 80 }
			//{ label: '供应商', name: 'supplier', index: 'supplier', width: 80 },
			//{ label: '备注', name: 'remarks', index: 'remarks', width: 80 },
			//{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 },
			//{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 },

        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    laydate.render({
        elem: '#checkDateBefore'
        , type: 'datetime'
        , range: false
        , done: function (value, date, endDate) {//控件选择完毕后的回调---点击日期、清空、现在、确定均会触发。
            vm.asset.buyTime = value;
        }
    });
});

/*标识分类树*/
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "categoryId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

/*标识部门树*/
var setting1 = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree1;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		asset: {
		    categoryId:null,
            categoryName:null,
            useOrgId:null,
            useOrgName:null,
            orgId:null,
            orgName:null


		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
            vm.asset = {categoryId:null, categoryName:null,useOrgId:null,
                useOrgName:null,
                orgId:null,
                orgName:null};
            //加载分类树
            vm.getCategory();

            //加载部门树
            vm.getDept();

            //加载区域下拉框。
            vm.getArea();

		},
        update: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            //加载分类树
            vm.getCategory();

            //加载部门树
            vm.getDept();

            //加载区域下拉框。
            vm.getArea();

            vm.getInfo(id);
        },
        getArea: function(){

            //清空
            $('#areaNameSelect').empty();
		    $.get(baseURL + "asset/assetarea/all", function (r) {
                if(r.code === 0){
                    var areaList = r.data;
                    // $("#productNameSelect").append("<option value='-1'>--请选择--</option>");
                    for (var i = 0; i < areaList.length; i++) {
                        $('#areaNameSelect').append("<option value="+areaList[i].areaId+">"+areaList[i].areaName+"</option>");
                    }
                }else{
                    layer.alert(r.msg);
                    $('#btnSaveOrUpdate').button('reset');
                    $('#btnSaveOrUpdate').dequeue();
                }
            })
        },

        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree1 = $.fn.zTree.init($("#deptTree"), setting1, r);
                var node = ztree1.getNodeByParam("deptId", vm.asset.orgId);
                if(node != null){
                    ztree1.selectNode(node);

                    vm.asset.orgName = node.name;
                }
            })
        },
        getCategory: function(){
            //加载部门树
            //list 不加入一级分类
            $.get(baseURL + "asset/assetcategory/list", function(r){
                ztree = $.fn.zTree.init($("#categoryTree"), setting, r);
                var node = ztree.getNodeByParam("categoryId", vm.asset.categoryId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.asset.categoryName = node.name;
                }
            })
        },

		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.asset.id == null ? "asset/asset/save" : "asset/asset/update";
                //下拉框 赋值
                vm.asset.empId = $("#empNameSelect option:selected").val();
                vm.asset.empName = $("#empNameSelect option:selected").text();
                vm.asset.areaId = $("#areaNameSelect option:selected").val();
                vm.asset.areaName = $("#areaNameSelect option:selected").text();

                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.asset),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "asset/asset/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "asset/asset/info/"+id, function(r){
                vm.asset = r.asset;
            });
		},

        categoryTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择分类",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.asset.categoryId = node[0].categoryId;
                    vm.asset.categoryName = node[0].name;

                    layer.close(index);
                }
            });
        },

        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree1.getSelectedNodes();
                    //选择上级部门
                    vm.asset.orgId = node[0].deptId;
                    vm.asset.orgName = node[0].name;

                    layer.close(index);
                }
            });
        },
        useDeptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree1.getSelectedNodes();
                    //选择上级部门
                    vm.asset.useOrgId = node[0].deptId;
                    vm.asset.useOrgName = node[0].name;

                    //清空
                    $('#empNameSelect').empty();
                    //点击使用部门后， 加载该部门下方的人员。
                    $.get(baseURL + "sys/user/getByDeptId/"+ vm.asset.useOrgId, function(r){
                        if(r.code === 0){
                            var userList = r.data;
                            // $("#productNameSelect").append("<option value='-1'>--请选择--</option>");
                            for (var i = 0; i < userList.length; i++) {
                                $('#empNameSelect').append("<option value="+userList[i].userId+">"+userList[i].username+"</option>");
                            }
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }

                    });

                    layer.close(index);
                }
            });


        },

		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
		}
	}
});
