<template>
    <div style="padding: 10px">
        <!--    功能区域-->
        <div  style="margin: 10px 0">
            <el-button type="primary" @click="add">新增</el-button>
            <el-button type="primary">导入</el-button>
            <el-button type="primary">导出</el-button>
        </div>
        <!--    搜索区域-->
        <div style="margin: 10px 0">
            <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable>

            </el-input>

            <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
        </div>
        <!--    表格区域-->
        <el-table
                :data="tableData"
                border
                style="width: 100%">
            <el-table-column
                    prop="id"
                    label="ID">
            </el-table-column>

            <el-table-column
                    prop="title"
                    label="标题">
            </el-table-column>
            <el-table-column
                    prop="author"
                    label="作者">
            </el-table-column>
            <el-table-column
                    prop="time"
                    label="时间">
            </el-table-column>

            <el-table-column
                    fixed="right"
                    label="操作">
                <template #default="scope">
                    <el-button @click="details(scope.row)" type="text">详情</el-button>

                    <el-button @click="handleEdit(scope.row)" type="text" v-if="user.role === 1">编辑</el-button>

                    <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
                        <template #reference>
                            <el-button type="text" v-if="user.role === 1">删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div style="margin: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>

        </div>


        <el-dialog title="提示" v-model="dialogVisible" width="50%">
            <el-form :model="form" label-width="120px">
                <el-form-item label="标题">
                    <el-input v-model="form.title" style="width: 50%"></el-input>
                </el-form-item>

                <div id="div1">

                </div>

<!--                <el-form-item label="内容">-->
<!--                    <el-input v-model="form.content" style="width: 80%"></el-input>-->
<!--                </el-form-item>-->

            </el-form>
            <template #footer>

               <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
               </span>

            </template>

        </el-dialog>

        <el-dialog title="详情" v-model="vis" width="50%">
            <el-card>
                <div v-html="detail.content" style="min-height: 100px">

                </div>
            </el-card>
        </el-dialog>

    </div>
</template>

<script>
    import request from "../utils/request";
    import E from 'wangeditor';

    let editor;

    export default {
        name: 'News',
        components:{

        },
        data() {

            return {
                form:{},
                dialogVisible:false,
                search:'',
                currentPage:1,
                pageSize:10,
                total:0,
                tableData: [],
                detail: {},
                vis: false
            }

        },
        mounted() {

        },
        created() {
            let userStr = sessionStorage.getItem("user") || "{}"
            this.user = JSON.parse(userStr)

            request.get("/user/" + this.user.id).then(res => {
                if(res.code == '0'){
                    this.user = res.data
                }
            })
            this.load()
        },
        methods:{
            filesUploadSuccess(res){
                console.log(res)
                this.form.cover = res.data
            },
            load(){
                request.get("/news",{
                    params:{
                        pageNum: this.currentPage,
                        pageSize: this.pageSize,
                        search: this.search
                    }
                }).then(res => {
                    console.log(res)
                    this.tableData = res.data.records
                    this.total = res.data.total

                })
            },
            add() {
                this.dialogVisible = true
                this.form = {}

                this.$nextTick(()=>{
                    //关联弹窗里面的div，new一个eidtor对象用于做文本编辑器
                    editor = new E('#div1')

                    //配置serve端接口
                    editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
                    editor.config.uploadFileName = 'file'
                    editor.create()
                })
            },
            save(){

                this.form.content = editor.txt.html()   //获取编辑器的值并赋予到实体

                this.dialogVisible = true
                if(this.form.id){//更新
                    request.put("/news", this.form).then(res => {
                        console.log(res)
                        if(res.code === '0') {
                            this.$message({
                                type: "success",
                                message: "更新成功"
                            })
                        }else {
                            this.$message({
                                type: "error",
                                message: res.msg
                            })
                        }
                        this.load() //刷新数据
                        this.dialogVisible = false  //关闭表格
                    })
                }
                else {//新增
                    let userStr =  sessionStorage.getItem("user") || "{}"
                    let user = JSON.parse(userStr)
                    this.form.author = user.nickName
                    request.post("/news", this.form).then(res => {
                        console.log(res)
                        if(res.code === '0') {
                            this.$message({
                                type: "success",
                                message: "新增成功"
                            })
                        }else {
                            this.$message({
                                type: "error",
                                message: res.msg
                            })
                        }
                        this.load() //刷新数据
                        this.dialogVisible = false  //关闭表格
                    })
                }


            },
            handleEdit(row){
                this.form = JSON.parse(JSON.stringify(row))
                this.dialogVisible = true
                this.$nextTick(()=>{
                    //关联弹窗里面的div，new一个eidtor对象用于做文本编辑器
                    editor = new E('#div1')
                    //配置serve端接口
                    editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
                    editor.config.uploadFileName = 'file'
                    editor.create()
                    editor.txt.html(row.content)
                })
            },
            handleDelete(id){
                console.log(id)
                request.delete("/news/" + id).then(res => {
                    console.log(res)
                    if(res.code === '0') {
                        this.$message({
                            type: "success",
                            message: "删除成功"
                        })
                    }else {
                        this.$message({
                            type: "error",
                            message: res.msg
                        })
                    }
                    this.load() //刷新数据
                })
            },
            handleSizeChange(pageSize){     //改变d当前每页的个数触发
                this.pageSize = pageSize
                this.load()
            },
            handleCurrentChange(pageNum){//    改变当前页码触发
                this.pageNum = pageNum
                this.load()
            },
            details(row){
                this.detail = row
                this.vis = true
            }
        }
    }
</script>
