const signInBtn = document.getElementById("signIn");
const signUpBtn = document.getElementById("signUp");
const fistForm = document.getElementById("form1");
const secondForm = document.getElementById("form2");
const container = document.querySelector(".container");

signInBtn.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

signUpBtn.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});

fistForm.addEventListener("submit", (e) => e.preventDefault());
secondForm.addEventListener("submit", (e) => e.preventDefault());

const Toast = Swal.mixin({
    toast: true,
    position: 'center-center',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})

function signup() {
    let username = $('#username').val();
    let password = $('#password').val();
    let passwordConfirm = $('#passwordConfirm').val();
    let passwordConfirmation = $('#password-check').val();
    let nickname = $('#nickname').val();

    if (username == "") {
        Swal.fire({
            icon: 'warning',
            title: '아이디 입력오류',
            text: '아이디가 공백입니다. 문자를 입력해주세요.',
        });
        $('#username').focus();
        return false;
    }

    if (password == "") {
        Swal.fire({
            icon: 'warning',
            title: '비밀번호 입력오류',
            text: '비밀번호가 공백입니다. 문자를 입력해주세요.',
        });
        $('#password').focus();
        return false;
    }

    if (passwordConfirm == "") {
        Swal.fire({
            icon: 'warning',
            title: '비밀번호 입력오류',
            text: '비밀번호 확인란이 공백입니다. 문자를 입력해주세요.',
        });
        $('#passwordConfirm').focus();

        if (passwordConfirmation == "") {
            Swal.fire({
                icon: 'warning',
                title: '이메일 입력오류',
                text: '비밀번호 확인란이 공백입니다. 문자를 입력해주세요.',
            });
            $('#password-check').focus();
            return false;
        }

        if (nickname == "") {
            Swal.fire({
                icon: 'warning',
                title: '닉네임 입력오류',
                text: '닉네임이 공백입니다. 문자 입력해주세요.',
            });
            $('#nickname').focus();
            return false;
        }

        if (password !== passwordConfirm) {
            Swal.fire({
                icon: 'warning',
                title: '비밀번호 확인 불일치',
                text: '비밀번호가 일치하지 않습니다. 다시 입력해주세요.',
            });
            $('#password').val('');
            $('#passwordConfirm').val('');
            $('#password').focus();
            return false;
        }

        $.ajax({
            type: "POST",
            url: `/api/user/signup`,
            contentType: "application/json",
            data: JSON.stringify({
                username: username,
                password: password,
                passwordConfirm: passwordConfirm,
                nickname: nickname
            }),
        })
            .done(function (res, status, xhr) {
                if (res.success) {
                    Toast.fire({
                        icon: 'success',
                        title: '회원가입에 성공했습니다.'
                    }).then(function () {
                        window.location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: '회원가입에 실패했습니다.',
                        text: '이미 존재하는 ID 이거나 nickname 입니다.'
                    });
                }
                Toast.fire({
                    icon: 'success',
                    title: '회원가입에 성공하셨습니다.'
                }).then(function () {
                    window.location.reload();
                })
            })
            .fail(function (jqXHR, textStatus, error) {
                console.log(error);
                Toast.fire({
                    icon: 'error',
                    title: '회원가입에 실패했습니다.'
                });
            });
    }

    function onLogin() {
        let loginUsername = $('#loginUsername').val();
        let loginPassword = $('#loginPassword').val();

        $.ajax({
            type: "POST",
            url: `/api/user/login`,
            contentType: "application/json",
            data: JSON.stringify({username: loginUsername, password: loginPassword}),
        })
            .done(function (res, status, xhr) {
                const token = xhr.getResponseHeader('Authorization');

                Cookies.set('Authorization', token, {path: '/'})

                $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
                    jqXHR.setRequestHeader('Authorization', token);
                });

                Toast.fire({
                    icon: 'success',
                    title: loginUsername + '님 환영합니다!'
                }).then(function () {
                    window.location.href = '/';
                })
            })
            .fail(function (jqXHR, textStatus) {
                Toast.fire({
                    icon: 'warning',
                    title: '로그인 정보를 확인해주시기 바랍니다.'
                })
            });
    }

    function onclickAdmin() {
        // Get the checkbox
        var checkBox = document.getElementById("admin-check");
        // Get the output text
        var box = document.getElementById("admin-token");

        // If the checkbox is checked, display the output text
        if (checkBox.checked == true) {
            box.style.display = "block";
        } else {
            box.style.display = "none";
        }
    }
}