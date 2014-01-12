class Location < ActiveRecord::Base
  belongs_to :user
  belongs_to :coordinate
  has_many :detections
  has_and_belongs_to_many :territories

  def to_hash
    hash = Hash[self.attributes]
    hash[:location_id] = hash[:id]
    hash.delete :id
    hash
  end
end
