package server;

import com.ZMPrinter.conn.ConnectException;
import common.CommonClass;
import common.LogType;
import data_processing.ErrorCatcher;
import data_processing.JsonData;
import data_processing.LabelBuilder;
import function.FuncLabelCreator;
import function.FunctionalException;
import utils.DataUtils;

public class DataProcessing {

    public static void process(String remoteAddress, String socketMessage) {
        try {
            // 如果是Json
            JsonData jsonData = DataUtils.fromJson(socketMessage);
            LabelBuilder.build(jsonData, remoteAddress);
        } catch (FunctionalException | ConnectException e) {
            // lsf模板打印的时候选择了preview抛出的异常、未定义的Operator抛出的异常、lsf文件找不到路径异常、json反序列化填充数据异常
            // 新增打印机连接的异常
            CommonClass.saveAndShow(remoteAddress + "    " + ErrorCatcher.CatchConnectError(e.getMessage()), LogType.ErrorData);
            ChannelMap.writeMessageToClient(remoteAddress, ErrorCatcher.CatchConnectError(e.getMessage()));
        } catch (Exception e) { // json序列化异常
            if (socketMessage.startsWith("{")) {
                String message = ErrorCatcher.CatchConnectError("4006|Json格式有误: ") + e.getMessage();
                CommonClass.saveAndShow(remoteAddress + "    " + message, LogType.ErrorData);
                ChannelMap.writeMessageToClient(remoteAddress, message);
            } else {
                // 函数式调用
                try {
                    FuncLabelCreator.analysis(remoteAddress, socketMessage);
                } catch (ConnectException | FunctionalException exception) {
                    String message = ErrorCatcher.CatchConnectError(exception.getMessage());
                    CommonClass.saveAndShow(remoteAddress + "    " + message, LogType.ErrorData);
                    ChannelMap.writeMessageToClient(remoteAddress, message);
                } catch (Exception otherException) {
                    String message = ErrorCatcher.CatchConnectError("4005|其他异常:") + otherException.getMessage();
                    CommonClass.saveAndShow(remoteAddress + "    " + message, LogType.ErrorData);
                    ChannelMap.writeMessageToClient(remoteAddress, message);
                }
            }
        }
    }

}
