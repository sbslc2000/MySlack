import styled from "styled-components";
import MessageUserIcon from "./MessageUserIcon";
import MessageBodyWithName from "./MessageBodyWithName";
import MessageBody from "./MessageBody";

const Wrapper = styled.div`
    padding-left:20px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
    padding-top:16px;
`;

function MessageWithName(props) {

    const user = props.message.sender;
    return (
        <Wrapper>
            <MessageUserIcon src={user.profileImage}/>
            <MessageBodyWithName message={props.message}/>
        </Wrapper>
    );
}

export default MessageWithName;