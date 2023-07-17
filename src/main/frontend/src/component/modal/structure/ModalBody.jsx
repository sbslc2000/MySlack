import styled from "styled-components";


const Body = styled.div`
    border-bottom:0px;
    padding-top:0px;
`;



function ModalBody(props) {
    return (
        <Body className="modal-body">
            {props.children}
        </Body>
    )
}

export default ModalBody;