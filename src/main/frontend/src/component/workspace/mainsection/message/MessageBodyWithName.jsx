import styled from "styled-components";
import MessageBody from "./MessageBody";

const Wrapper = styled.div`
    width: 1584px;
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
                <span style={{paddingLeft:5,fontSize:14}}>{message.createdAt}</span>
            </div>
            <MessageBody>
                <span>{message.content}</span>
            </MessageBody>
        </Wrapper>
    )

}

export default MessageBodyWithName;