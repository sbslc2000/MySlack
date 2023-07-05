import styled from "styled-components";

const Button = styled.button`
    margin-top:5px;
    width:100%;
   
`;


function DefaultButton(props) {

    return (
        <Button onClick={props.onClick} className={"btn btn-primary"}>
            {props.title}
        </Button>
    );
}

export default DefaultButton;