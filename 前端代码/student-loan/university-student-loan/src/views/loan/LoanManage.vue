<script setup>
import { useUserStore } from '@/stores'
import { Edit, Delete, Search, Check, Close } from '@element-plus/icons-vue'
import {
  StudentList,
  deleteLoan,
  schoolList,
  loanAgree,
  loanDisAgree
} from '@/api/loan'
import { ref, nextTick } from 'vue'
import LoanDetail from '@/components/LoanDetail.vue'
const userStore = useUserStore()
const stuList = ref([])
const loading = ref(false)
const drawer = ref(false)
const Detail = ref()
const total = ref(0)
const page = ref({
  pageNum: 1,
  pageSize: 5
})

//学生获取贷款列表
const getStuList = async () => {
  loading.value = true
  const {
    data: { data }
  } = await StudentList()
  stuList.value = data
  loading.value = false
}
getStuList()
const formatState = (row, column, cellValue) => {
  if (cellValue === 1) {
    return '待审核'
  } else if (cellValue === 2) {
    return '审核通过'
  } else {
    return '审核未通过'
  }
}
const formatType = (row, column, cellValue) => {
  if (cellValue === 1) {
    return '奖学金'
  } else if (cellValue === 2) {
    return '助学金'
  } else {
    return '助学贷款'
  }
}

const schList = ref()
//学校获取贷款列表
const getSchList = async () => {
  loading.value = true
  const res = await schoolList(page.value)
  total.value = res.data.data.total
  schList.value = res.data.data.items
  loading.value = false
}
getSchList()

//编辑
const onEdit = (row) => {
  drawer.value = true
  // 使用 nextTick 来确保 Detail 被正确创建和挂载
  nextTick(() => {
    // 访问 ref 并确保 Detail 组件已经被正确创建
    if (Detail.value && Detail.value.open) {
      Detail.value.open(row)
    }
  })
}

//删除
const onDelete = async (row) => {
  await deleteLoan(row.id)
  ElMessage.success('删除成功!')
  getStuList()
}

//修改成功
const onSuccess = () => {
  drawer.value = false
  getStuList()
}

//每页条数切换
const handleSizeChange = (size) => {
  page.value.size = size
  page.value.pageNum = 1
  getSchList()
}
//切换页数
const handleCurrentChange = (num) => {
  page.value.pageNum = num
  getSchList()
}

//查看
const onCheck = (row) => {
  drawer.value = true
  // 使用 nextTick 来确保 Detail 被正确创建和挂载
  nextTick(() => {
    // 访问 ref 并确保 Detail 组件已经被正确创建
    if (Detail.value && Detail.value.open) {
      Detail.value.open(row)
    }
  })
}

//审核通过
const onAgree = async (row) => {
  await loanAgree(row.id)
  ElMessage.success('资助已通过')
  getSchList()
  page.value.pageNum = 1
}
//审核不通过
const onDisAgree = async (row) => {
  await loanDisAgree(row.id)
  ElMessage.success('资助已驳回')
  getSchList()
  page.value.pageNum = 1
}
</script>
<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span style="font-weight: bold; font-size: 16px">贷款管理</span>
      </div>
    </template>
    <!-- 学生界面 -->
    <el-table
      :data="stuList"
      stripe
      style="width: 100%"
      v-if="userStore.user.usertype === 1"
      v-loading="loading"
    >
      <el-table-column label="序号" width="180" type="index"></el-table-column>
      <el-table-column
        prop="createTime"
        label="申请日期"
        sortable
        width="180"
      />
      <el-table-column
        prop="loanType"
        label="贷款类型"
        width="180"
        :formatter="formatType"
      />
      <el-table-column prop="loanLevel" label="贷款等级" />
      <el-table-column
        prop="state"
        label="状态"
        :formatter="formatState"
      ></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button
            type="primary"
            :icon="Edit"
            circle
            v-if="row.state === 1"
            @click="onEdit(row)"
          ></el-button>
          <el-button
            type="danger"
            :icon="Delete"
            circle
            v-if="row.state === 1"
            @click="onDelete(row)"
          ></el-button
        ></template>
      </el-table-column>
    </el-table>
    <!-- 校方界面 -->
    <el-table
      :data="schList"
      stripe
      style="width: 100%"
      v-loading="loading"
      v-else
    >
      <el-table-column label="序号" width="180" type="index"></el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="180" />
      <el-table-column prop="createUser" label="申请人学号" />
      <el-table-column prop="loanType" label="申请类型" />
      <el-table-column prop="loanLevel" label="申请等级" />
      <el-table-column label="操作" width="300">
        <template #default="{ row }">
          <el-button round :icon="Search" @click="onCheck(row)">查看</el-button>
          <el-button round type="success" :icon="Check" @click="onAgree(row)"
            >通过</el-button
          >
          <el-button round type="danger" :icon="Close" @click="onDisAgree(row)"
            >驳回</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 20px; justify-content: flex-end"
      v-model:current-page="page.pageNum"
      v-model:page-size="page.pageSize"
      :page-sizes="[2, 3, 5, 10]"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      v-if="userStore.user.usertype !== 1"
    />
  </el-card>
  <el-drawer v-model="drawer">
    <LoanDetail ref="Detail" @success="onSuccess"></LoanDetail>
  </el-drawer>
</template>
