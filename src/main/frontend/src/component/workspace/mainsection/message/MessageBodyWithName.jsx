import styled from "styled-components";
import MessageBody from "./MessageBody";
import DateUtil from "../../../../util/DateUtil";

const Wrapper = styled.div`
    display:flex;
    flex-direction:column;
    padding-left:10px;
    vertical-align:middle;
    
     &:hover{
        background-color:#222529;
    }
`
function MessageBodyWithName(props) {
    const message = props.message;
    return (
        <Wrapper>
            <div style={{height:22,fontSize:16,display:"inline-block"}}>
                <span style={{fontWeight:900}}>{message.sender.nickname}</span>
                <span style={{paddingLeft:5,fontSize:14}}>{DateUtil.getDateInMessage(new Date(message.createdAt))}</span>
            </div>
            <MessageBody>
                <span>{message.content}</span>
            </MessageBody>
        </Wrapper>
    )

}

export default MessageBodyWithName;