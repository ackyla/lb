class Location < ActiveRecord::Base
  belongs_to :user
  has_many :detections
  has_many :territories, :through => :detections
end
