import React, { Component } from "react";
import ButtonWithLoader from "../component/ButtonWithLoader";
import FormInput from "../component/FormInput";

export default class UserSignupPage extends Component {
  state = {
    displayName: "",
    username: "",
    password: "",
    repeatPassword: "",
    pendingApiCall: false,
    errors: {},
    passwordRepeatConfirmed: true,
  };

  onChangeUsername = (e) => {
    const value = e.target.value;
    const errors = { ...this.state.errors };
    delete errors.username;
    this.setState({
      username: value,
      errors,
    });
  };

  onChangeDisplayName = (e) => {
    const value = e.target.value;
    const errors = { ...this.state.errors };
    delete errors.displayName;
    this.setState({
      displayName: value,
      errors,
    });
  };

  onChangePassword = (e) => {
    const value = e.target.value;
    const passwordRepeatConfirmed = this.state.repeatPassword === value;
    const errors = { ...this.state.errors };
    delete errors.password;
    errors.passwordRepeat = passwordRepeatConfirmed
      ? ""
      : "Does not match to password";
    this.setState({
      password: value,
      passwordRepeatConfirmed,
      errors,
    });
  };

  onChangeRepeatPassword = (e) => {
    const value = e.target.value;
    const passwordRepeatConfirmed = this.state.password === value;
    const errors = { ...this.state.errors };
    errors.passwordRepeat = passwordRepeatConfirmed
      ? ""
      : "Does not match to password";
    this.setState({
      repeatPassword: value,
      passwordRepeatConfirmed,
      errors,
    });
  };

  onClickSignUp = () => {
    const user = {
      username: this.state.username,
      displayName: this.state.displayName,
      password: this.state.password,
    };
    this.setState({
      pendingApiCall: true,
    });
    this.props.actions
      .postSignUp(user)
      .then((response) => {
        this.setState(
          {
            pendingApiCall: false,
          },
          () => {
            this.props.history.push("/");
          }
        );
      })
      .catch((apiError) => {
        let errors = { ...this.state.errors };
        if (apiError.response.data && apiError.response.data.validationErrors) {
          errors = { ...apiError.response.data.validationErrors };
        }
        this.setState({
          pendingApiCall: false,
          errors,
        });
      });
  };

  getDisplayNameValidationError = () => {
    return this.state.errors.displayName ? this.state.errors.displayName : null;
  };
  render() {
    return (
      <div className="container">
        <h2
          id="signup-form-header"
          className="center indigo-text text-darken-2"
        >
          Sign Up
        </h2>
        <form className="col s12 m6 offset-m3">
          <FormInput
            placeholder="Your display name"
            label="Display name"
            id="display-name"
            value={this.state.displayName}
            onChange={this.onChangeDisplayName}
            hasError={this.state.errors.displayName && true}
            error={this.state.errors.displayName}
          />
          <FormInput
            placeholder="Your username"
            label="Username"
            id="user-name"
            value={this.state.username}
            onChange={this.onChangeUsername}
            hasError={this.state.errors.username && true}
            error={this.state.errors.username}
          />
          <FormInput
            type="password"
            placeholder="Your password"
            label="Password"
            id="password"
            value={this.state.password}
            onChange={this.onChangePassword}
            hasError={this.state.errors.password && true}
            error={this.state.errors.password}
          />
          <FormInput
            type="password"
            placeholder="Repeat your password"
            label="Repeat your password"
            id="repeat-password"
            value={this.state.repeatPassword}
            onChange={this.onChangeRepeatPassword}
            hasError={this.state.errors.passwordRepeat && true}
            error={this.state.errors.passwordRepeat}
          />
        </form>
        <div className="center">
          <ButtonWithLoader
            label="Sign Up"
            pendingApiCall={this.state.pendingApiCall}
            onClickAction={this.onClickSignUp}
            disabled={!this.state.passwordRepeatConfirmed}
          />
        </div>
      </div>
    );
  }
}

UserSignupPage.defaultProps = {
  actions: {
    postSignUp: () =>
      new Promise((resolve, reject) => {
        resolve({});
      }),
  },
  history: {
    push: () => {},
  },
};
