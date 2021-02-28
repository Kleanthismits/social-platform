import React from "react";

const ButtonWithLoader = (props) => {
  return props.pendingApiCall ? (
    <div className="preloader-wrapper small active" id="spinner-loading">
      <div className="spinner-layer spinner-blue-only">
        <div className="circle-clipper left">
          <div className="circle"></div>
        </div>
        <div className="gap-patch">
          <div className="circle"></div>
        </div>
        <div className="circle-clipper right">
          <div className="circle"></div>
        </div>
      </div>
    </div>
  ) : (
    <button
      className="btn waves-effect waves-light indigo"
      onClick={props.onClickAction}
      disabled={props.disabled}
    >
      {props.label}
    </button>
  );
};

export default ButtonWithLoader;
