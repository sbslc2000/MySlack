import MessageBody from "./MessageBody";
import styled from "styled-components";
const Wrapper = styled.div`
    padding-top:5px;
    padding-left:66px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
`;
const MessageWithoutName = (props) => {
    return (
        <Wrapper>
        <MessageBody>{props.message.content}</MessageBody>
        </Wrapper>
    )

}

export default MessageWithoutName;