from .models import Post, Comment, Chat, Message
from rest_framework import serializers

class PostSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source='user.username', read_only=True)

    class Meta:
        model = Post
        fields = '__all__'

class CommentSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source='user.username', read_only=True)

    class Meta:
        model = Comment
        fields = '__all__'

class ChatSerializer(serializers.ModelSerializer):
    user_one_name = serializers.CharField(source='user_one.username', read_only=True)
    user_two_name = serializers.CharField(source='user_two.username', read_only=True)
    class Meta:
        model = Chat
        fields = '__all__'

class MessageSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='user.username', read_only=True)

    class Meta:
        model = Message
        fields = '__all__'