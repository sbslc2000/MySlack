import styled from "styled-components";

const Footer = styled.div`
    border-top:0px;
    display:flex;
    justify-content:space-between;
`

function ModalFooter(props) {
    return (
        <Footer className="modal-footer">
            {props.children}
        </Footer>
    );
}

export default ModalFooter;

