#!/bin/sh

eval "$(ssh-agent -s)" && ssh-add ~/.ssh/id_ed25519
sleep infinity
