package main

import (
	"encoding/json"
	"fmt"
	"bytes"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

type CasesChaincode struct{

	//病例链码
}
//2. 入院日期， 手术情况， 入院诊断， 出院诊断， 入院情况，入院经过，出院情况，出院医嘱

type Cases struct{
	ObjectType string `json:"objectType"` //对象类型
	RandomNum string  `json:"RandomNum"`  //随机数，用作key值

	IDNo string  `json:"IDNo"`    //ID号

	//个人信息
	Name string `json:"name"`   //姓名
	Sex string `json:"Sex"`    //性别
	Birthday string `json:"Birthday"`      //生日
	Phone  string `json:"Phone"`  //联系电话
	PublicKey string `json:"PublicKey"` //公钥

	//病例信息
	HistoryOfDisease  string `json:"HistoryOfDisease"` //疾病史
	AdmissionDate string `json:"AdmissionDate"` //入院日期
	AdmissionDiagnosis string `json:"AdmissionDiagnosis"` //入院诊断
	OperationStatus string `json:"OperationStatus"` //手术情况
	DischargedDate string `json:"DischargedDate"` //出院日期
	DischargedDiagnosis string `json:"DischargedDiagnosis"` //出院诊断
	DischargedOrder string `json:"DischargedOrder"` //出院医嘱
	DischargedRecord string `json:"DischargedRecord"` //出院记录
	ImagingExamination string `json:"ImagingExamination"` //影像学检查
	LabExamination string `json:"LabExamination"` //实验室检查
	InvasiveProcedure string `json:"InvasiveProcedure"` //侵入性操作
}

//"addCase","o2kso22o3n","IDNo","Name","Sex","1991-08-26","13850921387","Ox2138jwiejk221jhewe","HistoryOfDisease","AdmissionDate","AdmissionDiagnosis","OperationStatus","DischargedDate","DischargedDiagnosis","DischargedOrder","DischargedRecord","ImagingExamination","LabExamination","InvasiveProcedure"

//实例化链码
func (t *CasesChaincode) Init (stub shim.ChaincodeStubInterface) peer.Response {
	return shim.Success(nil)
}

//链码操作
func (t *CasesChaincode) Invoke (stub shim.ChaincodeStubInterface) peer.Response {

	fn , args := stub.GetFunctionAndParameters()

	if fn == "addCase" {  //初始化病例
		return t.addCase(stub, args)
	} else if fn == "readCases" { //获取所有病例
		return t.readCases(stub, args)
	}  else if fn == "getCasesByRange"{ //获取指定范围的病例
		return t.getCasesByRange(stub,args)
	} else if fn == "queryCasesByOwner" {  //根据拥有者获取病例
		return t.queryCasesByOwner(stub, args)
	} 

	return shim.Error("没有相应的方法！")
}

func (t *CasesChaincode) addCase (stub shim.ChaincodeStubInterface, args [] string) peer.Response{

	//根据身份证信息来存储病例
	

	//判断 Cases 是否存在
	// CasesAsBytes , err := stub.GetState(Casesname)

	// if err != nil {
	// 	return shim.Error(err.Error())
	// }

	// if CasesAsBytes != nil {
	// 	return shim.Error("Cases 已经存在！")
	// }
	RandomNum :=args[0]
	IDNo := args[1]
	Name := args[2]
	Sex := args[3]
	Birthday:= args[4]
	Phone := args[5]
	PublicKey := args[6]
	HistoryOfDisease := args[7]
	AdmissionDate := args[8]
	AdmissionDiagnosis := args[9]
	OperationStatus := args[10]
	DischargedDate := args[11]
	DischargedDiagnosis := args[12]
	DischargedOrder := args[13]
	DischargedRecord := args[14]
	ImagingExamination := args[15]
	LabExamination := args[16]
	InvasiveProcedure := args[17]
	


	
	objectType := "Cases"
	Cases := &Cases{objectType,RandomNum,IDNo,Name,Sex,Birthday,Phone,PublicKey,HistoryOfDisease,AdmissionDate,
		AdmissionDiagnosis,OperationStatus,DischargedDate,DischargedDiagnosis,DischargedOrder,DischargedRecord,ImagingExamination,LabExamination,InvasiveProcedure}
	
	CasesJsonAsBytes, err := json.Marshal(Cases)

	err = stub.PutState(RandomNum,CasesJsonAsBytes)

	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *CasesChaincode) readCases(stub shim.ChaincodeStubInterface, args [] string) peer.Response{

	IDNo := args[0]

	CasesAsBytes , err := stub.GetState(IDNo)

	if err != nil {
		return shim.Error(err.Error())
	} else if  CasesAsBytes == nil {
		return shim.Error("Cases 信息不存在！")
	}

	return shim.Success(CasesAsBytes)
}



func(t *CasesChaincode) getCasesByRange( stub shim.ChaincodeStubInterface, args []string) peer.Response{

	startKey := args[0]
	endKey := args[1]

	resultIterator, err := stub.GetStateByRange(startKey,endKey)

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultIterator.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")

	isWrite := false
	for resultIterator.HasNext() {
		queryResponse, err := resultIterator.Next()

		if err != nil {
			return shim.Error(err.Error())
		}

		if isWrite == true {
			buffer.WriteString(",")
		}

		buffer.WriteString("{ \"key\": ")
		buffer.WriteString(queryResponse.Key)

		buffer.WriteString(",\"record\":")
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		isWrite = true
	}

	buffer.WriteString("]")

	return shim.Success(buffer.Bytes())
}

func (t * CasesChaincode) queryCasesByOwner (stub shim.ChaincodeStubInterface, args [] string) peer.Response {

	IDNo := args[0]
	queryStr := fmt.Sprintf("{\"selector\":{\"IDNo\":\"%s\"}}",IDNo)

	resultIterator, err := stub.GetQueryResult(queryStr)
	if err != nil {
		return shim.Error(err.Error())
	}

	defer resultIterator.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")

	isWrite := false 

	for resultIterator.HasNext() {
		queryResponse , err := resultIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		if isWrite == true {
			buffer.WriteString(",")
		}

		buffer.WriteString("{\"key\":")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString(",\"record\": ")
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		isWrite = true
	}
	buffer.WriteString("]")

	return shim.Success(buffer.Bytes())
}


func main() {

	err := shim.Start(new(CasesChaincode))
	if err != nil {
		fmt.Println("chaincode start error!")
	}
}