#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_3ed83aec37b2_key -iv $encrypted_3ed83aec37b2_iv -in travis/codesigning.asc.enc -out travis/codesigning.asc -d
    gpg --fast-import travis/codesigning.asc
fi