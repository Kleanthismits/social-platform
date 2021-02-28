import React from "react";

const FormInput = (props) => {
  const {
    hasError,
    error,
    label,
    id,
    type,
    value,
    onChange,
    placeholder,
  } = props;
  let inputClassName = "validate";
  if (hasError !== undefined) {
    inputClassName += hasError && " invalid";
  }
  return (
    <div className="row">
      <div className="input-field col  s12 m6 offset-m3">
        <input
          id={id}
          type={type || "text"}
          className={inputClassName}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
        />
        {label && (
          <label className="active" htmlFor={id}>
            {label}
          </label>
        )}
        {hasError && (
          <span
            id={id + "-error-span"}
            className="helper-text"
            data-error={error}
          >
            {error}
          </span>
        )}
      </div>
    </div>
  );
};

FormInput.defaultProps = {
  onChange: () => {},
};

export default FormInput;
