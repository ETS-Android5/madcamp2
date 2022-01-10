from rest_framework import serializers
from .models import Areas, Reviews

class AreasSerializer(serializers.ModelSerializer):
    class Meta:
        model = Areas
        fields = ('city', 'county', 'last')

class ReviewsSerializer(serializers.ModelSerializer):
    areas = AreasSerializer(many=True)

    class Meta:
        model = Reviews
        fields = ('area', 'sight', 'touch', 'taste')

    def create(self, validated_data):
        area_data = validated_data.pop('area')
        review = Reviews.objects.create(**validated_data)