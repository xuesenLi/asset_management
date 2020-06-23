$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/assetrefund/list',
        datatype: "json",
        colModel: [
            { label: '单据状态', name: 'recordStatus', index: 'record_status', width: 60, formatter:function (value, options, row) {
                    if(value === 0)
                        return '<span class="label label-warning">待审批</span>';
                    else if(value === 1)
                        return '<span class="label label-success">已同意</span>';
                    else if(value === 2)
                        return '<span class="label label-default">被驳回</span>';
                    else if(value === 3)
                        return '<span> -- </span>';
                } },
            /*
                        { label: 'id', name: 'id', index: 'id', width: 50, key: true },
            */
			{ label: '退还单号', name: 'recordNo', index: 'record_no', width: 100,
                formatter: function (value, options, row) {
                    return '<a style="cursor: pointer" onclick="vm.recordNoDetail(\'' + value + '\')">' + value + '</a>'
                }
            },
			{ label: '资产数量', name: 'assetNum', index: 'asset_num', width: 80 },
			{ label: '退还日期', name: 'actualTime', index: 'actual_time', width: 80 },
			{ label: '退还备注', name: 'recordRemarks', index: 'record_remarks', width: 80 },
/*
			{ label: '申请人id', name: 'createdUserid', index: 'created_userid', width: 80 },
*/
			{ label: '申请人', name: 'createdUsername', index: 'created_username', width: 80 },
			{ label: '申请日期', name: 'createdTime', index: 'created_time', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: false,
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
            vm.assetRefund.actualTime = value;
        }
    });


});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		assetRefund: {},

        //资产退还弹框
        changeOwnerShow: false,

	},
	methods: {

        /**点击单号 查看详情 */
        recordNoDetail: function(value){
            /**
             * iframe 弹出层
             */
            layer.open({
                type: 2,
                title: "退还单号详情",
                maxmin: true,
                shadeClose: true,
                shade: 0.5,
                area: ['98vw', '98vh'],
                content: 'transformer.html?recordNo=' + value
            });
        },

        /** 资产领用弹框 */
        updateOwner: function () {

            /**获取弹框表单数据 */
            vm.createOwnerTb();

            layer.open({
                type: 1,
                title: "资产退还",
                maxmin: true,
                shadeClose: true,
                shade: 0.5,
                area: ['80vw', '80vh'],
                content: $("#changeOwnerShow"),
                cancel:function(){
                    $("#oQMobile").val('');
                    $("#oQUserName").val('');
                }
            });
        },

        /**获取弹框表单数据 */
        createOwnerTb: function () {

            $("#ownerjqGrid").jqGrid({
                url: baseURL + "asset/asset/listByTypeZY",
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
                    { label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true },
                    { label: '资产编码', name: 'assetCode', index: 'asset_code', width: 80 },
                    { label: '资产名称', name: 'assetName', index: 'asset_name', width: 80 },
                    { label: '资产分类名称', name: 'categoryName', index: 'category_name', width: 80 },
                    { label: '所属组织名称', name: 'orgName', index: 'org_name', width: 80 },
                    { label: '使用组织名称', name: 'useOrgName', index: 'use_org_name', width: 80 },
                    { label: '使用人名称', name: 'empName', index: 'emp_name', width: 80 },
                    { label: '管理员名称', name: 'adminUsername', index: 'admin_username', width: 80 },
                    { label: '区域名称', name: 'areaName', index: 'area_name', width: 80 },
                    { label: '购买日期', name: 'buyTime', index: 'buy_time', width: 80 }
                ],
                viewrecords: true,
                // height: 385,
                rowNum: 10,
                rowList: [10, 30, 50],
                rownumbers: true,
                rownumWidth: 40,
                autowidth: true,
                multiselect: true,
                pager: "#ownerjqGridPager",
                jsonReader: {
                    root: "page.list",
                    page: "page.currPage",
                    total: "page.totalPage",
                    records: "page.totalCount"
                },
                prmNames: {
                    page: "page",
                    rows: "limit",
                    order: "order"
                },
                gridComplete: function () {
                    //隐藏grid底部滚动条
                    $("#ownerjqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
                    $("#ownerjqGrid").setGridHeight($(window).height() * 0.5);
                    $("#ownerjqGrid").setGridWidth($(window).width() * 0.75);
                }
            });

            //vm.isCreateTbed = true;
        },

        /*在弹框中选择多条记录**/
        getSelectedRows1: function(){
            var grid = $("#ownerjqGrid");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条记录");
                return ;
            }

            return grid.getGridParam("selarrrow");
        },

        save: function (event){
            var ids = vm.getSelectedRows1();
            if(ids == null){
                return ;
            }
            //选择的资产集合
            vm.assetRefund.assets = ids;
            console.log(ids);
            $('#btnSave').button('loading').delay(1000).queue(function() {
                var url = "asset/assetrefund/save";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.assetRefund),
                    success: function(r){
                        if(r.code === 0){
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnSave').button('reset');
                            $('#btnSave').dequeue();
                            //刷新页面
                            window.location.reload();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSave').button('reset');
                            $('#btnSave').dequeue();
                        }
                    }
                });
            });
        },



        query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.assetRefund = {};
		},
		/*update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.assetRefund.id == null ? "asset/assetrefund/save" : "asset/assetrefund/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.assetRefund),
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
                        url: baseURL + "asset/assetrefund/delete",
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
		},*/

		getInfo: function(id){
			$.get(baseURL + "asset/assetrefund/info/"+id, function(r){
                vm.assetRefund = r.assetRefund;
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
