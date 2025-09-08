FROM python

ADD requirements.txt ./requirements.txt
ADD relationshipServer.py ./relationshipServer.py

EXPOSE 25001

RUN pip3 install -r requirements.txt # fastapi pydantic uvicorn 

CMD "./relationshipServer.py"
