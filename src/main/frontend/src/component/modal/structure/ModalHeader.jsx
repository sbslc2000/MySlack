import styled from "styled-components";


const Header = styled.div`
    padding-top:20px;
    padding-bottom:20px;
    border-bottom:0px;
    display:flex;
    flex-direction:column;
`;

function ModalHeader(props) {

    return (
        <Header  className="modal-header">
            {props.children}
        </Header>
    )

}

export default ModalHeader;